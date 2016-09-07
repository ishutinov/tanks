package pt.pmendes.tanks.internal.entities;

import pt.pmendes.tanks.util.Properties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by pmendes.
 */
public class Tank extends pt.pmendes.tanks.internal.entities.BaseModel {
    public static final int TANK_WIDTH = 22;
    public static final int TANK_HEIGHT = 52;
    private static final int TANK_MAX_BACKWARDS_SPEED = -2;
    private static final int TANK_MAX_FORWARD_SPEED = 6;
    private static final double TANK_ACCELERATION = 0.3;
    private static final double TANK_ROTATION_SPEED = 3;

    private Turret turret;

    private int width = TANK_WIDTH;
    private int height = TANK_HEIGHT;
    private String color;
    private double speed;
    private int bulletCount = 0;
    private int killCount = 0;

    private Map<String, Boolean> isVisibleToMap = new HashMap<String, Boolean>();

    public Tank(String id, Tuple<Double> startPosition) {
        super(id, startPosition.getX(), startPosition.getY());
        int tankRotation = ThreadLocalRandom.current().nextInt(0, 359);
        setRotation(tankRotation);
        turret = new Turret(id, startPosition.getX(), startPosition.getY(), tankRotation);
        isVisibleToMap.put(getId(), true);

    }

    public Tuple<Double> calculateNewPosition(double speed) {
        return new Tuple<Double>(calculateNewX(speed), calculateNewY(speed));
    }

    private Double calculateNewX(double speed) {
        double radians = Math.toRadians(getRotation() - 90);
        double delta;
        if (speed > 0) {
            delta = speed + TANK_HEIGHT / 2;
        } else {
            delta = speed - TANK_HEIGHT / 2;
        }
        return getPosX() + delta * Math.cos(radians);
    }

    private Double calculateNewY(double speed) {
        double radians = Math.toRadians(getRotation() - 90);
        double delta;
        if (speed > 0) {
            delta = speed + TANK_HEIGHT / 2;
        } else {
            delta = speed - TANK_HEIGHT / 2;
        }
        return getPosY() + delta * Math.sin(radians);
    }

    public boolean canMove(WorldMap map, Tuple<Double> toPosition, Collection<Tank> tanks) {
        if (willCollideWithBoundries(toPosition.getX(), toPosition.getY(), map.getWidth(), map.getHeight())) {
            return false;
        }
        for (Tank tank : tanks) {
            // check to see if this tank is colliding with the other tanks
            if (tank.getId().equals(getId())) {
                continue;
            }
            if (toPosition.getX() >= (tank.getPosX() - tank.getWidth()) && toPosition.getX() <= (tank.getPosX() + tank.getWidth()) &&
                    toPosition.getY() >= (tank.getPosY() - tank.getHeight()) && toPosition.getY() <= (tank.getPosY() + tank.getHeight())) {
                return false;
            }
        }
        for (Wall wall : map.getWalls()) {
            // check to see if is colliding with inner walls
            if (toPosition.getX() >= wall.getPosX() && toPosition.getX() <= (wall.getPosX() + wall.getWidth()) &&
                    toPosition.getY() >= wall.getPosY() && toPosition.getY() <= (wall.getPosY() + wall.getHeight())) {
                return false;
            }
        }
        return true;
    }


    public void rotateTankLeft() {
        double newRotation = (getRotation() - TANK_ROTATION_SPEED);
        double dr = newRotation - getRotation();
        getTurret().setRotation(getTurret().getRotation() + dr);
        setRotation(newRotation);
    }

    public void rotateTankRight() {
        double newRotation = (getRotation() + TANK_ROTATION_SPEED);
        double dr = newRotation - getRotation();
        getTurret().setRotation(getTurret().getRotation() + dr);
        setRotation(newRotation);
    }

    public void rotateTankTurretLeft() {
        double rotation = getRotation() - Turret.TURRET_TURNING_SPEED;
        double dr = rotation - getRotation();
        getTurret().setRotation(getTurret().getRotation() + dr);
    }


    public void rotateTankTurretRight() {
        double rotation = getRotation() + Turret.TURRET_TURNING_SPEED;
        double dr = rotation - getRotation();
        getTurret().setRotation(getTurret().getRotation() + dr);
    }

    public void moveForward() {
        this.speed = accelerateForward();
        move();
    }

    public void moveBackWard() {
        this.speed = accelerateBackward();
        move();
    }

    private void move() {
        double radians = Math.toRadians(getRotation() - 90);
        setPosX(getPosX() + speed * Math.cos(radians));
        setPosY(getPosY() + speed * Math.sin(radians));
        getTurret().setPosX(getPosX());
        getTurret().setPosY(getPosY());
    }

    public double getBulletStartingPositionX() {
        double radians = Math.toRadians(turret.getRotation() - 90);
        return getPosX() + ((getTurret().getHeight()) * Math.cos(radians));
    }

    public double getBulletStartingPositionY() {
        double radians = Math.toRadians(turret.getRotation() - 90);
        return getPosY() + ((getTurret().getHeight()) * Math.sin(radians));
    }

    public boolean isCollidingWith(pt.pmendes.tanks.internal.entities.BaseModel model) {
        return (Math.abs(getPosX() - model.getPosX()) <= Tank.TANK_WIDTH)
                && (Math.abs(getPosY() - model.getPosY()) <= Tank.TANK_WIDTH);
    }

    public boolean hasFiredBullet(pt.pmendes.tanks.internal.entities.Bullet bullet) {
        return bullet.getTankId().equals(getId());
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getSpeed() {
        return speed;
    }

    public double accelerateForward() {
        if (this.speed < 0) {
            this.speed = 0;
        }
        double speed = this.speed + TANK_ACCELERATION;
        if (speed >= TANK_MAX_FORWARD_SPEED) {
            speed = TANK_MAX_FORWARD_SPEED;
        }
        return speed;
    }

    public double accelerateBackward() {
        if (this.speed > 0) {
            this.speed = 0;
        }
        double speed = this.speed - TANK_ACCELERATION;
        if (speed < TANK_MAX_BACKWARDS_SPEED) {
            speed = TANK_MAX_BACKWARDS_SPEED;
        }
        return speed;
    }

    public boolean canFireBullet() {
        return bulletCount < Properties.MAX_BULLET_COUNT_PER_TANK;
    }

    public synchronized void increaseBulletCount() {
        if (this.bulletCount <= Properties.MAX_BULLET_COUNT_PER_TANK) {
            this.bulletCount += 1;
        }
    }

    public void decreaseBulletCount() {
        if (bulletCount >= 0) {
            bulletCount -= 1;
        }
    }


    public boolean willCollideWithBoundries(double toX, double toY, int canvasWidth, int canvasHeight) {
        if (toX <= 0 || toX >= canvasWidth) {
            return true;
        }
        return toY <= 0 || toY >= canvasHeight;
    }

    /**
     * Returns true if the line from (a,b)->(c,d) intersects with (p,q)->(r,s)
     */
    private boolean intersects(double a, double b, double c, double d, double p, double q, double r, double s) {
        double det, gamma, lambda;
        det = (c - a) * (s - q) - (r - p) * (d - b);
        if (det == 0) {
            return false;
        } else {
            lambda = ((s - q) * (r - a) + (p - r) * (s - b)) / det;
            gamma = ((b - d) * (r - a) + (c - a) * (s - b)) / det;
            return (0 < lambda && lambda < 1) && (0 < gamma && gamma < 1);
        }
    }

    private boolean isVisibleTo(Tank tank, Collection<Wall> walls) {
        if (getId().equals(tank.getId())) {
            return true;
        }
        for (Wall wall : walls) {
            // intersects any of the wall lines
            if (intersects(getPosX(), getPosY(), tank.getPosX(), tank.getPosY(), wall.getPosX(), wall.getPosY(), wall.getPosX(), wall.getPosY() + wall.getHeight()) ||
                    intersects(getPosX(), getPosY(), tank.getPosX(), tank.getPosY(), wall.getPosX(), wall.getPosY(), wall.getPosX() + wall.getWidth(), wall.getPosY()) ||
                    intersects(getPosX(), getPosY(), tank.getPosX(), tank.getPosY(), wall.getPosX() + wall.getWidth(), wall.getPosY(), wall.getPosX() + wall.getWidth(), wall.getPosY() + wall.getHeight()) ||
                    intersects(getPosX(), getPosY(), tank.getPosX(), tank.getPosY(), wall.getPosX(), wall.getPosY() + wall.getHeight(), wall.getPosX() + wall.getWidth(), wall.getPosY() + wall.getHeight())) {
                return false;
            }
        }
        return true;
    }


    public Map<String, Boolean> getIsVisibility() {
        return isVisibleToMap;
    }

    public void setVisibility(Collection<Tank> tanks, Collection<Wall> walls) {
        for (Tank tank : tanks) {
            boolean isVisible = isVisibleTo(tank, walls);
            isVisibleToMap.put(tank.getId(), isVisible);
            tank.getIsVisibility().put(getId(), isVisible);
        }
    }

    public int getKillCount() {
        return killCount;
    }

    public void increaseKillCount() {
        killCount += 1;
    }

    public Turret getTurret() {
        return turret;
    }
}
