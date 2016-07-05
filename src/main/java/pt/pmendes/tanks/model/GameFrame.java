package pt.pmendes.tanks.model;


import java.util.HashMap;
import java.util.Map;

public class GameFrame {

    private WorldMap map;
    private Map<String, Tank> tanks;

    public GameFrame(WorldMap worldMap, HashMap<String, Tank> tanks) {
        this.map = worldMap;
        this.tanks = tanks;
    }

    public WorldMap getMap() {
        return map;
    }

    public Map<String, Tank> getTanks() {
        return tanks;
    }
}
