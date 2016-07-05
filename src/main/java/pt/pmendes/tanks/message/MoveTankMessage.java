package pt.pmendes.tanks.message;

/**
 * Created by pmendes on 07/05/2016.
 */
public class MoveTankMessage implements Message {
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
}
