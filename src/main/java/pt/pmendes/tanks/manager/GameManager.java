package pt.pmendes.tanks.manager;

import org.springframework.stereotype.Component;
import pt.pmendes.tanks.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GameManager {
    public final static int CANVAS_WIDTH = 640;
    public final static int CANVAS_HEIGHT = 480;

    private GameFrame gameFrame;

    public GameManager() {
        this.gameFrame = new GameFrame(new WorldMap(CANVAS_WIDTH, CANVAS_HEIGHT));
    }

    public synchronized Tank moveTank(String tankId, double speed, double rotation) {
        Tank tank = null;
        if (gameFrame.getTanks().containsKey(tankId)) {
            tank = gameFrame.getTanks().get(tankId);
            if (canMove(tankId, tank.calculateNewX(speed), tank.calculateNewY(speed))) {
                tank.setSpeed(speed);
                tank.setRotation(rotation);
                tank.move();
            }
        }
        return tank;
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
            if ((Math.abs(tank.getPosX() - toX) <= Tank.TANK_WIDTH ||
                    Math.abs(tank.getPosX() - toX) <= Tank.TANK_HEIGHT)
                    && (Math.abs(tank.getPosY() - toY) <= Tank.TANK_WIDTH ||
                    Math.abs(tank.getPosY() - toY) <= Tank.TANK_HEIGHT)) {
                return false;
            }
        }
        return true;
    }

    public void fireBullet(String tankId) {
        Tank tank = getTank(tankId);
        if (tank != null) {
            Bullet newBullet = new Bullet(UUID.randomUUID().toString(), tank.getId(), tank.getPosX(), tank.getPosY(), tank.getRotation());
            getGameFrame().getBullets().put(newBullet.getId(), newBullet);
        }
    }

    public void updateGameFrame() {
        Collection<Bullet> bullets = new ArrayList<Bullet>(getBullets());
        for (Bullet bullet : bullets) {
            bullet.move();
            removeOutOfBoundsBullets(bullet);
            updateTanks(bullet);
        }
    }

    private void updateTanks(Bullet bullet) {
        Collection<Tank> tanks = new ArrayList<Tank>(getTanks());
        for (Tank tank : tanks) {
            if (!tank.hasFiredBullet(bullet) && tank.isCollidingWith(bullet)) {
                gameFrame.removeTank(tank.getId());
                gameFrame.removeBullet(bullet.getId());
            }
        }
    }

    private void removeOutOfBoundsBullets(Bullet bullet) {
        if (bullet.isOutOfBounds()) {
            gameFrame.removeBullet(bullet.getId());
        }
    }

    public Tank getTank(String tankId) {
        return gameFrame.getTanks().get(tankId);
    }

    private Collection<Tank> getTanks() {
        return getGameFrame().getTanks().values();
    }

    private Collection<Bullet> getBullets() {
        return getGameFrame().getBullets().values();
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void addGameMessage(GameMessage gameMessage) {
        gameFrame.addGameMessage(gameMessage);
    }
}
