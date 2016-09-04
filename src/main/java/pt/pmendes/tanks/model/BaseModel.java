package pt.pmendes.tanks.model;

import pt.pmendes.tanks.util.Properties;

/**
 * Created by pmendes.
 */
public abstract class BaseModel {
    private String id;
    private double posX;
    private double posY;
    private double rotation;

    public BaseModel(String id, double posX, double posY) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.rotation = 0;
    }

    public BaseModel(String id, double posX, double posY, double rotation) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.rotation = rotation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        if (this.rotation <= -360 || this.rotation >= 360) {
            this.rotation = 0;
        }
    }

    public double getRotation() {
        return rotation;
    }

    public boolean isCollidingWith(BaseModel model) {
        return this.posX == model.getPosX() && this.posY == model.getPosX();
    }

    public boolean isOutOfBounds(int canvasWidth, int canvasHeight) {
        return getPosX() < 0 || getPosY() < 0 || getPosX() >= canvasWidth || getPosY() >= canvasHeight;
    }
}
