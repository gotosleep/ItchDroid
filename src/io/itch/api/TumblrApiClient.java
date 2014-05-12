package io.itch.api;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TumblrApiClient {

    private static final String KEY = "fWnvNwLMtOaJxKhMJuJPUXa4n0SyNled0byq9dSwRN5hoQJPED";
    private static final Object INSTANCE_LOCK = new Object();
    private static TumblrApi SHARED_INSTANCE;

    // http://api.tumblr.com/v2/blog/citriccomics.tumblr.com/posts/text?api_key={key}
    public static TumblrApi getClient() {
        if (SHARED_INSTANCE == null) {
            synchronized (INSTANCE_LOCK) {
                if (SHARED_INSTANCE == null) {
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                            .create();
                    String endPoint = "http://api.tumblr.com/v2/blog/itchio.tumblr.com/";
                    RequestInterceptor requestInterceptor = new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addQueryParam("api_key", KEY);
                        }
                    };
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(endPoint)
                            .setConverter(new GsonConverter(gson))
                            .setRequestInterceptor(requestInterceptor)
                            .build();
                    SHARED_INSTANCE = adapter.create(TumblrApi.class);
                }
            }
        }
        return SHARED_INSTANCE;
    }
}
