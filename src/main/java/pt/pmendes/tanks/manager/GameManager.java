package pt.pmendes.tanks.manager;

import org.springframework.stereotype.Component;
import pt.pmendes.tanks.model.GameFrame;
import pt.pmendes.tanks.model.Tank;
import pt.pmendes.tanks.model.WorldMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GameManager {
    private final static int CANVAS_WIDTH = 640;
    private final static int CANVAS_HEIGHT = 480;

    private GameFrame gameFrame;

    public GameManager() {
        WorldMap worldMap = new WorldMap(CANVAS_WIDTH, CANVAS_HEIGHT);
        HashMap<String, Tank> tanks = new HashMap<String, Tank>();
        if (gameFrame == null) {
            this.gameFrame = new GameFrame(worldMap, tanks);
        }
    }

    public synchronized Tank moveTank(String tankId, double speed, double rotation) {
        if (gameFrame.getTanks().containsKey(tankId)) {
            Tank tank = gameFrame.getTanks().get(tankId);
            double projectX = tank.calculateNewX(speed);
            double projectY = tank.calculateNewY(speed);
            if (canMove(tankId, projectX, projectY)) {
                tank.setRotation(rotation);
                if (speed != 0) {
                    tank.move(speed);
                }
            }
        }
        return gameFrame.getTanks().get(tankId);
    }

    public synchronized Tank addTank(String tankId) {
        if (!gameFrame.getTanks().containsKey(tankId)) {
            int x = ThreadLocalRandom.current().nextInt(Tank.TANK_WIDTH, CANVAS_WIDTH - Tank.TANK_WIDTH);
            int y = ThreadLocalRandom.current().nextInt(Tank.TANK_WIDTH, CANVAS_HEIGHT - Tank.TANK_WIDTH);
            gameFrame.getTanks().put(tankId, new Tank(tankId, x, y));
        }
        return gameFrame.getTanks().get(tankId);
    }

    private boolean canMove(String tankId, double toX, double toY) {
        if (toX <= Tank.TANK_WIDTH || toX >= CANVAS_WIDTH) {
            return false;
        }
        if (toY <= Tank.TANK_WIDTH || toY >= CANVAS_HEIGHT) {
            return false;
        }
        for (Tank tank : getTanks()) {
            if (tank.getId().equals(tankId)) {
                continue;
            }
            if (Math.abs(tank.getPosX() - toX) <= Tank.TANK_WIDTH && Math.abs(tank.getPosY() - toY) <= Tank.TANK_WIDTH) {
                return false;
            }
        }
        return true;
    }

    public Tank getTank(String tankId) {
        return gameFrame.getTanks().get(tankId);
    }

    private Collection<Tank> getTanks() {
        return getGameFrame().getTanks().values();
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }


}
