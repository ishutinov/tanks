package pt.pmendes.tanks.ws.message;

import java.io.Serializable;

/**
 * Created by pmendes.
 */
public class MoveTankMessage implements Serializable {
    private String tankId;
    private double rotation;
    private double speed;

    public String getTankId() {
        return tankId;
    }

    public void setTankId(String tankId) {
        this.tankId = tankId;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String toString() {
        return "{tankId: " + tankId + ", speed: " + speed + "rotation: " + rotation + "}";
    }

}
