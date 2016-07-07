package pt.pmendes.tanks.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.concurrent.ThreadLocalRandom;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tank {
    public static final int TANK_WIDTH = 10;

    @XmlElement
    private String id;
    @XmlElement
    private double posX;
    @XmlElement
    private double posY;
    @XmlElement
    private double rotation;
    @XmlElement
    private String color;

    public Tank(String id, int posX, int posY) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.rotation = ThreadLocalRandom.current().nextInt(0, 359);
    }

    public Tank(String id, int posX, int posY, String color) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.rotation = ThreadLocalRandom.current().nextInt(0, 359);
    }

    public double calculateNewX(double speed) {
        double radians = Math.toRadians(this.rotation - 90);
        return this.posX + speed * Math.cos(radians);
    }

    public double calculateNewY(double speed) {
        double radians = Math.toRadians(this.rotation - 90);
        return this.posY + speed * Math.sin(radians);
    }

    public void move(double speed) {
        double radians = Math.toRadians(this.rotation - 90);
        this.posX += speed * Math.cos(radians);
        this.posY += speed * Math.sin(radians);
    }

    public double getPosX() {
        return posX;
    }

    private void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    private void setPosY(double posY) {
        this.posY = posY;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWidth() {
        return TANK_WIDTH;
    }

    public String getId() {
        return id;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        if (this.rotation <= -360 || this.rotation >= 360) {
            this.rotation = 0;
        }
    }
}
