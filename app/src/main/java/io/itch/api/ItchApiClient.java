package io.itch.api;

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

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

import io.itch.api.responses.GraphsResponse;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class ItchApiClient {

    private static final Object INSTANCE_LOCK = new Object();
    private static ItchApi SHARED_INSTANCE;
    private static String token;

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
                            .build();
                    SHARED_INSTANCE = adapter.create(ItchApi.class);
                }
            }
        }
        return SHARED_INSTANCE;
    }

    public static void setToken(String token) {
        ItchApiClient.token = token;
        synchronized (INSTANCE_LOCK) {
            SHARED_INSTANCE = null;
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
