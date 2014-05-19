package io.itch.api;

import io.itch.R;
import io.itch.api.responses.GraphsResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.util.Map;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.converter.GsonConverter;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ItchApiClient {

    private static ItchApi SHARED_INSTANCE;
    private static final Object INSTANCE_LOCK = new Object();
    private static String token;
    private static KeyStore keyStore;

    public static void loadKeyStore(Context context) {
        KeyStore trusted = null;
        InputStream in = null;

        try {
            trusted = KeyStore.getInstance("BKS");
            in = context.getResources().openRawResource(R.raw.itchstore);
            trusted.load(in, "ez24get".toCharArray());
        } catch (Exception e) {
            Log.e("Itch", "Failed to load keystore", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // don't care
                }
            }
            keyStore = trusted;
        }
    }

    public static ItchApi getClient() {
        if (SHARED_INSTANCE == null) {
            synchronized (INSTANCE_LOCK) {
                if (SHARED_INSTANCE == null) {
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                            .registerTypeAdapter(GraphsResponse.class, new GraphsDeserializer())
                            .create();
                    String endPoint = "https://itch.io/api/1";
                    if (!TextUtils.isEmpty(token)) {
                        endPoint = endPoint + "/" + token;
                    }
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(endPoint)
                            .setConverter(new GsonConverter(gson))
                            .setClient(new ApacheClient(new ItchHttpClient(keyStore)))
                            .build();
                    SHARED_INSTANCE = adapter.create(ItchApi.class);
                }
            }
        }
        return SHARED_INSTANCE;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ItchApiClient.token = token;
        synchronized (INSTANCE_LOCK) {
            SHARED_INSTANCE = null;
        }
    }

    private static class ItchHttpClient extends DefaultHttpClient {
        private final KeyStore keyStore;

        private ItchHttpClient(KeyStore keyStore) {
            super();
            this.keyStore = keyStore;
        }

        @Override
        protected ClientConnectionManager createClientConnectionManager() {
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", newSslSocketFactory(), 443));
            return new ThreadSafeClientConnManager(getParams(), registry);
        }

        private SSLSocketFactory newSslSocketFactory() {
            try {
                return new SSLSocketFactory(this.keyStore);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }

    // This is needed because the graphs API returns inconsistent types.
    // When there is data, it returns a json array. When there is no data, it returns an empty object
    private static class GraphsDeserializer implements JsonDeserializer<GraphsResponse> {

        @Override
        public GraphsResponse deserialize(JsonElement json, Type typeOfJson, JsonDeserializationContext context)
                throws JsonParseException {
            GraphsResponse result = new GraphsResponse();
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                    try {
                        Field field = GraphsResponse.class.getDeclaredField(entry.getKey());
                        field.setAccessible(true);
                        Object value;
                        if (entry.getValue().isJsonArray()) {
                            value = context.deserialize(entry.getValue(), field.getGenericType());
                        } else {
                            value = null;
                        }
                        field.set(result, value);
                    } catch (Exception e) {
                        Log.e("Itch", "Invalid field: " + e);
                    }
                }
            }
            return result;
        }
    }
}
