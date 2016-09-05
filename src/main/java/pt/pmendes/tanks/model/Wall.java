package pt.pmendes.tanks.model;

/**
 * Created by pmendes.
 */
public class Wall extends BaseModel {

    public static final int MIN_SIZE = 20;
    public static final int MAX_SIZE = 200;

    private final double width;
    private double height;

    public Wall(String id, double posX, double posY, double width, double height) {
        super(id, posX, posY);
        this.width = width;
        this.height = height;
    }


    public boolean isCollidingWith(BaseModel model) {
        return model.getPosX() >= getPosX() && model.getPosX() <= (getPosX() + width) &&
                model.getPosY() >= getPosY() && model.getPosY() <= (getPosY() + height);
    }

    public boolean contains(Tuple<Double> pos) {
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
