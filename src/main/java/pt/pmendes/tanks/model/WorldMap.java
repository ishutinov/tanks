package pt.pmendes.tanks.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by pmendes.
 */
public class WorldMap {
    private int width;
    private int height;
    private Collection<Wall> walls;

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.walls = new ArrayList<Wall>();

        this.walls.add(createRandomWall());
        this.walls.add(createRandomWall());
        this.walls.add(createRandomWall());
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Collection<Wall> getWalls() {
        return walls;
    }

    private Wall createRandomWall() {
        double wallPosX = ThreadLocalRandom.current().nextInt(10, width - 10);
        double wallPosY = ThreadLocalRandom.current().nextInt(10, height - 10);
        double width = 20; //ThreadLocalRandom.current().nextInt(10, 25);
        double height = ThreadLocalRandom.current().nextInt(40, 200);
        return new Wall(UUID.randomUUID().toString(), wallPosX, wallPosY, width, height);
    }
}
