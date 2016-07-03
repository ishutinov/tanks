package main.java.player;

import java.io.Serializable;

public class Player implements Serializable {

    private String id;

    private void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
