package io.itch.api;

import io.itch.models.MyGamesResponse;
import retrofit.Callback;
import retrofit.http.GET;

public interface ItchApi {

    @GET("/my-games")
    public void listMyGames(Callback<MyGamesResponse> callback);

}
