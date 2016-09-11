package pt.pmendes.tanks.internal.entities;

import java.util.UUID;

/**
 * Created by pmendes.
 */
public class Wall extends BaseModel {

    private static final double WALL_DEPTH = 50;
    private final double width;
    private double height;
    private double dept = WALL_DEPTH;

    public Wall(double posX, double posY, double width, double height) {
        super(UUID.randomUUID().toString(), posX, posY);
        this.width = width;
        this.height = height;
    }

    public Wall(double posX, double posY, double width, double height, double dept) {
        super(UUID.randomUUID().toString(), posX, posY);
        this.width = width;
        this.height = height;
        this.dept = dept;
    }


    public boolean isCollidingWith(BaseModel model) {
        return model.getPosX() >= (getPosX() - width / 2) && model.getPosX() <= (getPosX() + width / 2) &&
                model.getPosY() >= (getPosY() - height / 2) && model.getPosY() <= (getPosY() + height / 2);
    }

    public boolean contains(Tuple<Double> pos) {
        return pos.getX() >= (getPosX() - width / 2) && pos.getX() <= (getPosX() + width / 2) &&
                pos.getY() >= (getPosY() - height / 2) && pos.getY() <= (getPosY() + height / 2);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDept() {
        return dept;
    }
}
