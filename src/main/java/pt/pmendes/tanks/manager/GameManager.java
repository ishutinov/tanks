package pt.pmendes.tanks.manager;

import org.springframework.stereotype.Component;
import pt.pmendes.tanks.model.*;
import pt.pmendes.tanks.util.Properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by pmendes.
 */
@Component
public class GameManager {

    private GameFrame gameFrame;

    public GameManager() {
        this.gameFrame = new GameFrame(new WorldMap(Properties.CANVAS_WIDTH, Properties.CANVAS_HEIGHT));
    }

    public synchronized Tank moveTank(String tankId, double speed, double rotation) {
        Tank tank = null;
        if (gameFrame.getTanks().containsKey(tankId)) {
            tank = gameFrame.getTanks().get(tankId);
            if (canMove(tankId, tank.calculateNewPosition(speed))) {
                tank.setSpeed(speed);
                tank.setRotation(rotation);
                tank.move();
            }
        }
        return tank;
    }

    public synchronized Tank addTank(String tankId) {
        if (!gameFrame.getTanks().containsKey(tankId)) {
            int x = ThreadLocalRandom.current().nextInt(Tank.TANK_WIDTH, Properties.CANVAS_WIDTH - Tank.TANK_WIDTH);
            int y = ThreadLocalRandom.current().nextInt(Tank.TANK_WIDTH, Properties.CANVAS_HEIGHT - Tank.TANK_WIDTH);
            gameFrame.getTanks().put(tankId, new Tank(tankId, x, y));
        }
        return gameFrame.getTanks().get(tankId);
    }

    private boolean canMove(String tankId, Tuple<Double> toPosition) {
        if (getTank(tankId).willCollideWithBoundries(toPosition.getX(), toPosition.getY())) {
            return false;
        }
        for (Tank tank : getTanks()) {
            if (tank.getId().equals(tankId)) {
                continue;
            }
            if ((Math.abs(tank.getPosX() - toPosition.getX()) <= Tank.TANK_WIDTH ||
                    Math.abs(tank.getPosX() - toPosition.getX()) <= Tank.TANK_HEIGHT)
                    && (Math.abs(tank.getPosY() - toPosition.getY()) <= Tank.TANK_WIDTH ||
                    Math.abs(tank.getPosY() - toPosition.getY()) <= Tank.TANK_HEIGHT)) {
                return false;
            }
        }
        return true;
    }

    public void fireBullet(String tankId) {
        Tank tank = getTank(tankId);
        if (tank != null && tank.canFireBullet()) {
            Bullet newBullet = new Bullet(UUID.randomUUID().toString(), tank.getId(), tank.getBulletPositionX(), tank.getBulletPositionY(), tank.getRotation());
            tank.increaseBulletCount();
            getGameFrame().getBullets().put(newBullet.getId(), newBullet);
        }
    }

    public void updateGameFrame() {
        Collection<Bullet> bullets = new ArrayList<Bullet>(getBullets());
        for (Bullet bullet : bullets) {
            updateBullets(bullet);
            updateTanks(bullet);
        }
    }

    private void updateBullets(Bullet bullet) {
        bullet.move();
        removeOutOfBoundsBullets(bullet);
        for (Wall wall : gameFrame.getMap().getWalls()) {
            if (wall.isCollidingWith(bullet)) {
                gameFrame.removeBullet(bullet.getId());
                getTank(bullet.getTankId()).decreaseBulletCount();
            }
        }

    }

    private void updateTanks(Bullet bullet) {
        Collection<Tank> tanks = new ArrayList<Tank>(getTanks());
        for (Tank tank : tanks) {
            if (!tank.hasFiredBullet(bullet) && tank.isCollidingWith(bullet)) {
                gameFrame.removeTank(tank.getId());
                gameFrame.removeBullet(bullet.getId());
                tank.decreaseBulletCount();
                GameMessage message = new GameMessage();
                message.setPlayerId(tank.getId());
                message.setMessage(tank.getId() + " was killed by " + bullet.getTankId());
                gameFrame.addGameMessage(message);
            }
        }
    }

    private void removeOutOfBoundsBullets(Bullet bullet) {
        if (bullet.isOutOfBounds()) {
            gameFrame.removeBullet(bullet.getId());
            getTank(bullet.getTankId()).decreaseBulletCount();
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
