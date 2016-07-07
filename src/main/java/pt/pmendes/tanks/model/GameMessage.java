package pt.pmendes.tanks.model;

import java.util.Date;

/**
 * Created by pmendes.
 */
public class GameMessage {
    private String playerId;
    private String message;
    private Date timestamp;

    public GameMessage() {
        timestamp = new Date();
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
