package pt.pmendes.tanks.internal.entities;

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
    private Collection<pt.pmendes.tanks.internal.entities.Wall> walls;

    public WorldMap() {
        this.width = Properties.CANVAS_DEFAULT_WIDTH;
        this.height = Properties.CANVAS_DEFAULT_HEIGHT;
        this.walls = new ArrayList<pt.pmendes.tanks.internal.entities.Wall>();
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

    public Collection<pt.pmendes.tanks.internal.entities.Wall> getWalls() {
        return walls;
    }

    private pt.pmendes.tanks.internal.entities.Wall createRandomWall() {
        double wallPosX = ThreadLocalRandom.current().nextInt(10, width - 10);
        double wallPosY = ThreadLocalRandom.current().nextInt(10, height - 10);
        double width = ThreadLocalRandom.current().nextInt(pt.pmendes.tanks.internal.entities.Wall.MIN_SIZE, pt.pmendes.tanks.internal.entities.Wall.MAX_SIZE);
        double height = ThreadLocalRandom.current().nextInt(pt.pmendes.tanks.internal.entities.Wall.MIN_SIZE, pt.pmendes.tanks.internal.entities.Wall.MAX_SIZE);
        return new pt.pmendes.tanks.internal.entities.Wall(UUID.randomUUID().toString(), wallPosX, wallPosY, width, height);
    }
}
