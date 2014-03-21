package io.itch.api;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ItchApiClient {

    private static ItchApi SHARED_INSTANCE;
    private static final Object LOCK = new Object();
    private static String token;

    public static ItchApi getClient() {
        if (SHARED_INSTANCE == null) {
            synchronized (LOCK) {
                if (SHARED_INSTANCE == null) {
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                            .create();
                    String endPoint = "http://itch.io/api/1";
                    if (!TextUtils.isEmpty(token)) {
                        endPoint = endPoint + "/" + token;
                    }
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(endPoint)
                            .setConverter(new GsonConverter(gson))
                            .setErrorHandler(new MyErrorHandler())
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
        synchronized (LOCK) {
            SHARED_INSTANCE = null;
        }
    }

    private static class MyErrorHandler implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
            Response r = cause.getResponse();
            if (r != null && r.getStatus() == 401) {
                // return new UnauthorizedException(cause);
            }
            return cause;
        }
    }
}
