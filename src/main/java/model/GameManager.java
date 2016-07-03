package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GameManager {

    private static GameManager INSTANCE = null;

    private final static int WIDTH = 1024;
    private final static int HEIGHT = 768;

    @XmlElement
    private WorldMap map;
    @XmlElement
    private Map<String, Tank> tanks = new HashMap<String, Tank>();

    private GameManager() {
        // singleton
    }

    public static GameManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameManager(WIDTH, HEIGHT);
        }
        return INSTANCE;
    }

    public GameManager(int width, int height) {
        this.map = new WorldMap(width, height);
    }

    public synchronized Tank moveTank(String tankId, int posX, int posY) {
        if (tanks.containsKey(tankId)) {
            tanks.get(tankId).move(posX, posY);
        }
        return tanks.get(tankId);
    }

    public synchronized Tank addTank(String tankId) {
        if (!tanks.containsKey(tankId)) {
            int x = ThreadLocalRandom.current().nextInt(0, WIDTH - 5);
            int y = HEIGHT - 5;
            tanks.put(tankId, new Tank(x, y));
        }
        return tanks.get(tankId);
    }

    public synchronized Tank getTank(String tankId) {
        if (tanks.containsKey(tankId)) {
            return tanks.get(tankId);
        }
        return null;
    }


}
