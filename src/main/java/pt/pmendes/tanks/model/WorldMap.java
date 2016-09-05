package pt.pmendes.tanks.model;

import pt.pmendes.tanks.util.Properties;

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

    public WorldMap() {
        this.width = Properties.CANVAS_DEFAULT_WIDTH;
        this.height = Properties.CANVAS_DEFAULT_HEIGHT;
        this.walls = new ArrayList<Wall>();
        for (int i = 0; i < Properties.NUMBER_OF_INNER_WALLS; i++) {
            this.walls.add(createRandomWall());
        }
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
        double width = ThreadLocalRandom.current().nextInt(Wall.MIN_SIZE, Wall.MAX_SIZE);
        double height = ThreadLocalRandom.current().nextInt(Wall.MIN_SIZE, Wall.MAX_SIZE);
        return new Wall(UUID.randomUUID().toString(), wallPosX, wallPosY, width, height);
    }
}
