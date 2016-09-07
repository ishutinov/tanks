package pt.pmendes.tanks.internal.entities;

import java.util.UUID;

/**
 * Created by pmendes.
 */
public class Wall extends BaseModel {

    private final double width;
    private double height;

    public Wall(double posX, double posY, double width, double height) {
        super(UUID.randomUUID().toString(), posX, posY);
        this.width = width;
        this.height = height;
    }


    public boolean isCollidingWith(pt.pmendes.tanks.internal.entities.BaseModel model) {
        return model.getPosX() >= getPosX() && model.getPosX() <= (getPosX() + width) &&
                model.getPosY() >= getPosY() && model.getPosY() <= (getPosY() + height);
    }

    public boolean contains(pt.pmendes.tanks.internal.entities.Tuple<Double> pos) {
        return pos.getX() >= getPosX() && pos.getX() <= getPosX() + getWidth() &&
                pos.getY() >= getPosY() && pos.getY() <= getPosY() + getHeight();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
