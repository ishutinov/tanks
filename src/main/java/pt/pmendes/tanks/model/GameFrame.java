package pt.pmendes.tanks.model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameFrame {

    private WorldMap map;
    private Map<String, Tank> tanks;
    private Map<String, Bullet> bullets;
    private Collection<GameMessage> messages;

    public GameFrame(WorldMap worldMap) {
        this.map = worldMap;
        this.tanks = new HashMap<String, Tank>();
        this.bullets = new HashMap<String, Bullet>();
        this.messages = new ArrayList<GameMessage>();
    }

    public WorldMap getMap() {
        return map;
    }

    public Map<String, Tank> getTanks() {
        return tanks;
    }

    public Map<String, Bullet> getBullets() {
        return bullets;
    }

    public Collection<GameMessage> getMessages() {
        return messages;
    }

    public void removeTank(String id) {
        tanks.remove(id);
    }

    public void removeBullet(String id) {
        bullets.remove(id);
    }

    public void addGameMessage(GameMessage gameMessage) {
        messages.add(gameMessage);
    }
}
