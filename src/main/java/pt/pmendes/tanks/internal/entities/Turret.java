package pt.pmendes.tanks.internal.entities;

/**
 * Created by pssm on 06/09/16.
 */
public class Turret extends BaseModel {

    public static final int TURRET_WIDTH = 25;
    public static final int TURRET_HEIGHT = 69;
    public static final int TURRET_TURNING_SPEED = 3;

    private int width = TURRET_WIDTH;
    private int height = TURRET_HEIGHT;

    public Turret(String id, double posX, double posY, double rotation) {
        super(id, posX, posY, rotation);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
