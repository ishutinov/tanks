package pt.pmendes.tanks.internal.entities;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by pmendes.
 */
public class WorldMap {
    private int width;
    private int height;
    private Collection<Wall> walls;

    public WorldMap(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public void initMap() {
        this.walls = new ArrayList<Wall>();
        this.walls.add(createLeftWall());
        this.walls.add(createRightWall());
        this.walls.add(createTopWall());
        this.walls.add(createBottomWall());
        this.walls.add(createMiddleWall());
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


    private Wall createLeftWall() {
        double wallPosX = 200;
        double wallPosY = 150;
        double wallWidth = 40;
        double wallHeight = height - 300;
        return new Wall(wallPosX, wallPosY, wallWidth, wallHeight);
    }


    private Wall createRightWall() {
        double wallPosX = width - 200;
        double wallPosY = 150;
        double wallWidth = 40;
        double wallHeight = height - 300;
        return new Wall(wallPosX, wallPosY, wallWidth, wallHeight);
    }


    private Wall createTopWall() {
        double wallPosX = 400;
        double wallPosY = 100;
        double wallWidth = width - 800;
        double wallHeight = 40;
        return new Wall(wallPosX, wallPosY, wallWidth, wallHeight);
    }

    private Wall createBottomWall() {
        double wallPosX = 400;
        double wallPosY = height - 150;
        double wallWidth = width - 800;
        double wallHeight = 40;
        return new Wall(wallPosX, wallPosY, wallWidth, wallHeight);
    }

    private Wall createMiddleWall() {
        double wallPosX = (width / 2 - 20);
        double wallPosY = (height / 2 - 80);
        double wallWidth = 40;
        double wallHeight = 160;
        return new Wall(wallPosX, wallPosY, wallWidth, wallHeight);
    }

}
