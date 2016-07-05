package pt.pmendes.tanks.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tank {

    @XmlElement
    private String id;
    @XmlElement
    private int posX;
    @XmlElement
    private int posY;
    private int width = 5;
    @XmlElement
    private String color;

    public Tank(String id, int posX, int posY) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
    }

    public Tank(String id, int posX, int posY, String color) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.color = color;
    }

    public void move(int newPosX, int newPosY) {
        setPosX(newPosX);
        setPosY(newPosY);
    }

    public int getPosX() {
        return posX;
    }

    private void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    private void setPosY(int posY) {
        this.posY = posY;
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

    public String getId() {
        return id;
    }
}
