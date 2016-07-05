package pt.pmendes.tanks.manager;

import org.springframework.stereotype.Component;
import pt.pmendes.tanks.model.GameFrame;
import pt.pmendes.tanks.model.Tank;
import pt.pmendes.tanks.model.WorldMap;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GameManager {
    private final static int WIDTH = 1024;
    private final static int HEIGHT = 768;

    private GameFrame gameFrame;

    public GameManager() {
        WorldMap worldMap = new WorldMap(WIDTH, HEIGHT);
        HashMap<String, Tank> tanks = new HashMap<String, Tank>();
        if (gameFrame == null) {
            this.gameFrame = new GameFrame(worldMap, tanks);
        }
    }

    public synchronized Tank moveTank(String tankId, int posX, int posY) {
        if (gameFrame.getTanks().containsKey(tankId)) {
            gameFrame.getTanks().get(tankId).move(posX, posY);
        }
        return gameFrame.getTanks().get(tankId);
    }

    public synchronized Tank addTank(String tankId) {
        if (!gameFrame.getTanks().containsKey(tankId)) {
            int x = ThreadLocalRandom.current().nextInt(0, WIDTH - 5);
            int y = HEIGHT - 5;
            gameFrame.getTanks().put(tankId, new Tank(tankId, x, y));
        }
        return gameFrame.getTanks().get(tankId);
    }

    public Tank getTank(String tankId) {
        if (gameFrame.getTanks().containsKey(tankId)) {
            return gameFrame.getTanks().get(tankId);
        }
        return null;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }


}
