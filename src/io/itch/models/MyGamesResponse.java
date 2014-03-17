package io.itch.models;

import java.util.List;

public class MyGamesResponse {

    private List<Game> games;

    private MyGamesResponse(List<Game> games) {
        super();
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

}
