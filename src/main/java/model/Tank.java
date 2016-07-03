package model;

public class Tank {

    private int posX;
    private int posY;
    private int width = 5;
    private String color;

    public Tank(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Tank(int posX, int posY, String color) {
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
}
