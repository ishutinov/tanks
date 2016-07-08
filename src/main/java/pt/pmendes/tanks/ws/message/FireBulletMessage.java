package pt.pmendes.tanks.ws.message;

import java.io.Serializable;

/**
 * Created by pmendes.
 */
public class FireBulletMessage implements Serializable {

    private String tankId;

    public String getTankId() {
        return tankId;
    }

    public void setTankId(String tankId) {
        this.tankId = tankId;
    }
}
