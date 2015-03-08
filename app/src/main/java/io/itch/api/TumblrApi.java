package io.itch.api;

import io.itch.api.responses.PostsResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TumblrApi {

    @GET("/posts")
    public void listPosts(@Query("limit") Integer limit, Callback<PostsResponse> callback);

}
