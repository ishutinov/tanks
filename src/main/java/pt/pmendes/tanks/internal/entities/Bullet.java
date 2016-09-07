package pt.pmendes.tanks.internal.entities;

/**
 * Created by pmendes.
 */
public class Bullet extends BaseModel {
    private static final double BULLET_SPEED = 7;
    private static final int BULLET_RADIUS = 2;
    private static final int BULLET_DAMAGE = 20;

    private final String tankId;
    private final double damageModifier;

    public Bullet(String id, String tankId, double posX, double posY, double rotation) {
        super(id, posX, posY, rotation);
        this.tankId = tankId;
        this.damageModifier = 1;
    }

    public String getTankId() {
        return tankId;
    }

    public int getRadius() {
        return BULLET_RADIUS;
    }

    public void move() {
        double radians = Math.toRadians(getRotation() - 90);
        setPosX(getPosX() + BULLET_SPEED * Math.cos(radians));
        setPosY(getPosY() + BULLET_SPEED * Math.sin(radians));
    }

    public double getBulletDamage() {
        return BULLET_DAMAGE * damageModifier;
    }

}
