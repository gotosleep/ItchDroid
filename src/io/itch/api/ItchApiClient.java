package io.itch.api;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ItchApiClient {

    private static ItchApi SHARED_INSTANCE;
    private static final Object LOCK = new Object();

    public static ItchApi getClient() {
        if (SHARED_INSTANCE == null) {
            synchronized (LOCK) {
                if (SHARED_INSTANCE == null) {
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                            .create();
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint("http://itch.io/api/1/" + ItchApiKey.API_KEY)
                            .setConverter(new GsonConverter(gson))
                            .build();
                    SHARED_INSTANCE = adapter.create(ItchApi.class);
                }
            }
        }
        return SHARED_INSTANCE;
    }
}
