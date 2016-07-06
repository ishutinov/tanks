package pt.pmendes.tanks.message;

import java.io.Serializable;

/**
 * Created by pmendes.
 */
public class MoveTankMessage implements Serializable {
    private String tankId;
    private int posX;
    private int posY;

    public String getTankId() {
        return tankId;
    }

    public void setTankId(String tankId) {
        this.tankId = tankId;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public String toString() {
        return "{tankId: " + tankId + ", posX: " + posX + ", posY: " + posY + "}";
    }
}
