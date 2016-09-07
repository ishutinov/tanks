package pt.pmendes.tanks.internal.manager;

import org.springframework.stereotype.Component;
import pt.pmendes.tanks.api.GameDirector;
import pt.pmendes.tanks.api.TankDirector;
import pt.pmendes.tanks.internal.entities.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by pmendes.
 */
@Component
public class TanksGameDirector implements GameDirector, TankDirector {

    private Frame frame;

    public TanksGameDirector() {
        this.frame = new Frame(new WorldMap());
    }

    public void reset() {
        this.frame = new Frame(new WorldMap());
    }

    public void init(int canvasWidth, int canvasHeight) {
        getFrame().getMap().setWidth(canvasWidth);
        getFrame().getMap().setHeight(canvasHeight);
    }

    public synchronized Tank moveTankForward(String tankId) {
        Tank tank = null;
        if (frame.getTanks().containsKey(tankId)) {
            tank = frame.getTanks().get(tankId);
            if (tank.canMove(frame.getMap(), tank.calculateNewPosition(tank.accelerateForward()), getTanks())) {
                tank.moveForward();
                tank.setVisibility(getTanks(), getWalls());
            }
        }
        return tank;
    }

    public synchronized Tank moveTankBackward(String tankId) {
        Tank tank = null;
        if (frame.getTanks().containsKey(tankId)) {
            tank = frame.getTanks().get(tankId);
            if (tank.canMove(frame.getMap(), tank.calculateNewPosition(tank.accelerateBackward()), getTanks())) {
                tank.moveBackWard();
                tank.setVisibility(getTanks(), getWalls());
            }
        }
        return tank;
    }

    public synchronized Tank rotateTankLeft(String tankId) {
        Tank tank = null;
        if (frame.getTanks().containsKey(tankId)) {
            tank = frame.getTanks().get(tankId);
            tank.rotateTankLeft();
        }
        return tank;
    }

    public synchronized Tank rotateTankRight(String tankId) {
        Tank tank = null;
        if (frame.getTanks().containsKey(tankId)) {
            tank = frame.getTanks().get(tankId);
            tank.rotateTankRight();
        }
        return tank;
    }

    public Tank rotateTankTurretLeft(String tankId) {
        Tank tank = null;
        if (frame.getTanks().containsKey(tankId)) {
            tank = frame.getTanks().get(tankId);
            tank.rotateTankTurretLeft();
        }
        return tank;
    }

    public Tank rotateTankTurretRight(String tankId) {
        Tank tank = null;
        if (frame.getTanks().containsKey(tankId)) {
            tank = frame.getTanks().get(tankId);
            tank.rotateTankTurretRight();
        }
        return tank;
    }


    public synchronized Tank addTank(String tankId) {
        if (frame.getTanks().containsKey(tankId)) {
            frame.getTanks().remove(tankId);
        }
        Tuple<Double> position;
        while (true) {
            List<Boolean> isValid = new ArrayList<Boolean>();
            Double x = ThreadLocalRandom.current().nextDouble(Tank.TANK_WIDTH, getFrame().getMap().getWidth() - Tank.TANK_WIDTH);
            Double y = ThreadLocalRandom.current().nextDouble(Tank.TANK_WIDTH, getFrame().getMap().getHeight() - Tank.TANK_WIDTH);
            position = new Tuple<Double>(x, y);
            for (Wall wall : getWalls()) {
                isValid.add(!wall.contains(position));
            }
            if (!isValid.contains(Boolean.FALSE)) {
                break;
            }
        }
        frame.getTanks().put(tankId, new Tank(tankId, position));

        return frame.getTanks().get(tankId);
    }

    public void fireBullet(String tankId) {
        Tank tank = getTank(tankId);
        if (tank != null && tank.canFireBullet()) {
            Bullet newBullet = new Bullet(UUID.randomUUID().toString(), tank.getId(), tank.getBulletStartingPositionX(), tank.getBulletStartingPositionY(), tank.getTurret().getRotation());
            tank.increaseBulletCount();
            getFrame().getBullets().put(newBullet.getId(), newBullet);
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
        for (Wall wall : frame.getMap().getWalls()) {
            if (wall.isCollidingWith(bullet)) {
                frame.removeBullet(bullet.getId());
                getTank(bullet.getTankId()).decreaseBulletCount();
            }
        }

    }

    private void updateTanks(Bullet bullet) {
        Collection<Tank> tanks = new ArrayList<Tank>(getTanks());
        for (Tank tank : tanks) {
            if (!tank.hasFiredBullet(bullet) && tank.isCollidingWith(bullet)) {
                frame.removeTank(tank.getId());
                frame.removeBullet(bullet.getId());
                frame.getTanks().get(bullet.getTankId()).increaseKillCount();
                tank.decreaseBulletCount();
                Message message = new Message();
                message.setPlayerId(tank.getId());
                message.setMessage(tank.getId() + " was killed by " + bullet.getTankId());
                frame.addGameMessage(message);
            }
        }
    }

    private void removeOutOfBoundsBullets(Bullet bullet) {
        if (bullet.isOutOfBounds(getFrame().getMap().getWidth(), getFrame().getMap().getHeight())) {
            frame.removeBullet(bullet.getId());
            getTank(bullet.getTankId()).decreaseBulletCount();
        }
    }

    public Tank getTank(String tankId) {
        return frame.getTanks().get(tankId);
    }

    public Collection<Tank> getTanks() {
        return getFrame().getTanks().values();
    }

    private Collection<Bullet> getBullets() {
        return getFrame().getBullets().values();
    }

    private Collection<Wall> getWalls() {
        return getFrame().getMap().getWalls();
    }

    public Frame getFrame() {
        return frame;
    }

    public void addGameMessage(Message message) {
        frame.addGameMessage(message);
    }
}
