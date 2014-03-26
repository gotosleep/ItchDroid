package io.itch.api;

import io.itch.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.converter.GsonConverter;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
            return new SingleClientConnManager(getParams(), registry);
        }

        private SSLSocketFactory newSslSocketFactory() {
            try {
                return new SSLSocketFactory(this.keyStore);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }
}
