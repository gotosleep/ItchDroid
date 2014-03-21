package io.itch.api.responses;

import io.itch.models.Game;

import java.util.List;

public class GamesResponse {

    private List<Game> games;

    private GamesResponse(List<Game> games) {
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
