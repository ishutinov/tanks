package pt.pmendes.tanks.model;

import pt.pmendes.tanks.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by pmendes.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tank extends BaseModel {
    public static final int TANK_WIDTH = 22;
    public static final int TANK_HEIGHT = 52;
    private static final int TANK_MAX_BACKWARDS_SPEED = -3;
    private static final int TANK_MAX_FORWARD_SPEED = 8;

    @XmlElement
    private int width = TANK_WIDTH;
    @XmlElement
    private int height = TANK_HEIGHT;
    @XmlElement
    private String color;
    @XmlElement
    private double speed;
    private int bulletCount = 0;

    public Tank(String id, Tuple<Double> startPosition) {
        super(id, startPosition.getX(), startPosition.getY());
        setRotation(ThreadLocalRandom.current().nextInt(0, 359));
    }

    public Tank(String id, int posX, int posY, String color) {
        super(id, posX, posY);
        this.color = color;
        setRotation(ThreadLocalRandom.current().nextInt(0, 359));
    }

    public Tuple<Double> calculateNewPosition(double speed) {
        return new Tuple<Double>(calculateNewX(speed), calculateNewY(speed));
    }

    private Double calculateNewX(double speed) {
        double radians = Math.toRadians(getRotation() - 90);
        if (speed > 0) {
            return getPosX() + (speed + TANK_HEIGHT / 2) * Math.cos(radians);
        } else {
            return getPosX() + (speed - TANK_HEIGHT / 2) * Math.cos(radians);
        }
    }

    private Double calculateNewY(double speed) {
        double radians = Math.toRadians(getRotation() - 90);
        if (speed > 0) {
            return getPosY() + (speed + TANK_HEIGHT / 2) * Math.sin(radians);
        } else {
            return getPosY() + (speed - TANK_HEIGHT / 2) * Math.sin(radians);
        }
    }

    public boolean canMove(Tuple<Double> toPosition, Collection<Tank> tanks, Collection<Wall> walls) {
        if (willCollideWithBoundries(toPosition.getX(), toPosition.getY())) {
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
        for (Wall wall : walls) {
            // check to see if is colliding with inner walls
            if (toPosition.getX() >= wall.getPosX() && toPosition.getX() <= (wall.getPosX() + wall.getWidth()) &&
                    toPosition.getY() >= wall.getPosY() && toPosition.getY() <= (wall.getPosY() + wall.getHeight())) {
                return false;
            }
        }
        return true;
    }

    public void move() {
        if (speed == 0) {
            return;
        }
        double radians = Math.toRadians(getRotation() - 90);
        setPosX(getPosX() + speed * Math.cos(radians));
        setPosY(getPosY() + speed * Math.sin(radians));
    }

    public double getBulletPositionX() {
        double radians = Math.toRadians(getRotation() - 90);
        return getPosX() + ((TANK_WIDTH) * Math.cos(radians));
    }

    public double getBulletPositionY() {
        double radians = Math.toRadians(getRotation() - 90);
        return getPosY() + ((TANK_HEIGHT / 2) * Math.sin(radians));
    }

    public boolean isCollidingWith(BaseModel model) {
        return (Math.abs(getPosX() - model.getPosX()) <= Tank.TANK_WIDTH)
                && (Math.abs(getPosY() - model.getPosY()) <= Tank.TANK_WIDTH);
    }

    public boolean hasFiredBullet(Bullet bullet) {
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

    public void setSpeed(double speed) {
        if (speed < TANK_MAX_BACKWARDS_SPEED) {
            this.speed = TANK_MAX_BACKWARDS_SPEED;
        }
        if (speed > TANK_MAX_FORWARD_SPEED) {
            this.speed = TANK_MAX_FORWARD_SPEED;
        }
        this.speed = speed;
    }

    public boolean canFireBullet() {
        return bulletCount < Properties.MAX_BULLET_COUNT_PER_TANK;
    }

    public void increaseBulletCount() {
        if (this.bulletCount <= Properties.MAX_BULLET_COUNT_PER_TANK) {
            this.bulletCount += 1;
        }
    }

    public void decreaseBulletCount() {
        if (bulletCount >= 0) {
            this.bulletCount -= 1;
        }
    }


    public boolean willCollideWithBoundries(double toX, double toY) {
        if (toX <= TANK_HEIGHT || toX >= Properties.CANVAS_WIDTH) {
            return true;
        }
        if (toY <= TANK_HEIGHT || toY >= Properties.CANVAS_HEIGHT) {
            return true;
        }
        return false;
    }
}
