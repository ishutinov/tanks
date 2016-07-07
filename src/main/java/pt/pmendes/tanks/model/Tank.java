package pt.pmendes.tanks.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.concurrent.ThreadLocalRandom;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tank extends BaseModel {
    public static final int TANK_WIDTH = 22;
    public static final int TANK_HEIGHT = 52;

    @XmlElement
    private int width = TANK_WIDTH;
    @XmlElement
    private int height = TANK_HEIGHT;
    @XmlElement
    private String color;
    @XmlElement
    private double speed;

    public Tank(String id, int posX, int posY) {
        super(id, posX, posY);
        setRotation(ThreadLocalRandom.current().nextInt(0, 359));
    }

    public Tank(String id, int posX, int posY, String color) {
        super(id, posX, posY);
        this.color = color;
        setRotation(ThreadLocalRandom.current().nextInt(0, 359));
    }

    public double calculateNewX(double speed) {
        double radians = Math.toRadians(getRotation() - 90);
        return getPosX() + speed * Math.cos(radians);
    }

    public double calculateNewY(double speed) {
        double radians = Math.toRadians(getRotation() - 90);
        return getPosY() + speed * Math.sin(radians);
    }

    public void move() {
        if (speed == 0) {
            return;
        }
        double radians = Math.toRadians(getRotation() - 90);
        setPosX(getPosX() + speed * Math.cos(radians));
        setPosY(getPosY() + speed * Math.sin(radians));
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
        if (speed < -3) {
            this.speed = -3;
        }
        if (speed > 8) {
            this.speed = 8;
        }
        this.speed = speed;
    }
}
