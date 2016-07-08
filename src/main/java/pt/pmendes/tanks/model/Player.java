package pt.pmendes.tanks.model;

import java.io.Serializable;

/**
 * Created by pmendes.
 */
public class Player implements Serializable {

    private String id;

    private void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
