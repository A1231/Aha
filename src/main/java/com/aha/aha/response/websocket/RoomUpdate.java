package com.aha.aha.response.websocket;

import java.util.List;

public class RoomUpdate {


    List<String> players;

    public RoomUpdate(List<String> players) {
        this.players = players;
    }
    public List<String> getPlayers() {
        return players;
    }
    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
