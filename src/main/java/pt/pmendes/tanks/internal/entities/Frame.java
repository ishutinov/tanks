package pt.pmendes.tanks.internal.entities;


import java.util.*;

/**
 * Created by pmendes.
 */
public class Frame {

    private long timestamp;
    private WorldMap map;
    private Map<String, Tank> tanks;
    private Map<String, pt.pmendes.tanks.internal.entities.Bullet> bullets;
    private Collection<Message> messages;

    public Frame(WorldMap worldMap) {
        this.timestamp = new Date().getTime();
        this.map = worldMap;
        this.tanks = new HashMap<String, Tank>();
        this.bullets = new HashMap<String, pt.pmendes.tanks.internal.entities.Bullet>();
        this.messages = new ArrayList<Message>();
    }

    public WorldMap getMap() {
        return map;
    }

    public Map<String, Tank> getTanks() {
        return tanks;
    }

    public Map<String, pt.pmendes.tanks.internal.entities.Bullet> getBullets() {
        return bullets;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void removeTank(String id) {
        tanks.remove(id);
    }

    public void removeBullet(String id) {
        bullets.remove(id);
    }

    public void addGameMessage(Message message) {
        messages.add(message);
    }

    public long getTimestamp() {
        return timestamp;
    }
}
