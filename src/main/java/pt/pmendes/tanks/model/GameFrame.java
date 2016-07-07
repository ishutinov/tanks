package pt.pmendes.tanks.model;


import java.util.HashMap;
import java.util.Map;

public class GameFrame {

    private WorldMap map;
    private Map<String, Tank> tanks;
    private Map<String, Bullet> bullets;

    public GameFrame(WorldMap worldMap) {
        this.map = worldMap;
        this.tanks = new HashMap<String, Tank>();
        this.bullets = new HashMap<String, Bullet>();
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
}
