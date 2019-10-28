import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

import processing.core.PImage;

public final class WorldModel
{
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    
    private final int PROPERTY_KEY = 0;
    private final int MINER_NUM_PROPERTIES = 7;
    private final int ORE_NUM_PROPERTIES = 5;
    private final int VEIN_NUM_PROPERTIES = 5;

    private final int MINER_ID = 1;
    private final int MINER_COL = 2;
    private final int MINER_ROW = 3;
    private final int MINER_LIMIT = 4;
    private final int MINER_ACTION_PERIOD = 5;
    private final int MINER_ANIMATION_PERIOD = 6;

    private final int ORE_ID = 1;
    private final int ORE_COL = 2;
    private final int ORE_ROW = 3;
    private final int ORE_ACTION_PERIOD = 4;
    private final int ORE_REACH = 1;

    private final int VEIN_ID = 1;
    private final int VEIN_COL = 2;
    private final int VEIN_ROW = 3;
    private final int VEIN_ACTION_PERIOD = 4;

    private final int BGND_ID = 1;
    private final int BGND_COL = 2;
    private final int BGND_ROW = 3;

    private final int OBSTACLE_ID = 1;
    private final int OBSTACLE_COL = 2;
    private final int OBSTACLE_ROW = 3;

    private final int SMITH_ID = 1;
    private final int SMITH_COL = 2;
    private final int SMITH_ROW = 3;
    
    private final String BLOB_KEY = "blob";
    private final String QUAKE_KEY = "quake";
    private final String MINER_KEY = "miner";
    private final String ORE_KEY = "ore";
    private final String VEIN_KEY = "vein";
    private final String BGND_KEY = "background";
    private final String OBSTACLE_KEY = "obstacle";
    private final String SMITH_KEY = "blacksmith";

    private final int BGND_NUM_PROPERTIES = 4;
    private final int OBSTACLE_NUM_PROPERTIES = 4;
    private final int SMITH_NUM_PROPERTIES = 4;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public int getNumRows() { return this.numRows; }
    public int getNumCols() { return this.numCols; }
    public Background[][] getBackground(int numRows, int numCols) { return this.background; }
    public void setBackground(Background[][] background) { this.background = background; }
    public Entity[][] getOccupancy() { return this.occupancy; }
    public Set<Entity> getEntities() { return this.entities; }
    public String getORE_KEY() {return this.ORE_KEY;}
    public String getBLOB_KEY() {return this.BLOB_KEY;}
    public String getQUAKE_KEY() {return this.QUAKE_KEY;}

    public Optional<Point> findOpenAround(Point pos) {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
                Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
                if (withinBounds(newPt) && !isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        entity.addEntity(this, entity);
    }

    public boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0
                && pos.getX() < this.numCols;
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

        /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */

    public void setBackground(
            Point pos, Background background)
    {
        if (withinBounds(pos)) {
            setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.getY()][pos.getX()];
    }

    public void setOccupancyCell(
            Point pos, Entity entity)
    {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.getY()][pos.getX()];
    }

    public void setBackgroundCell(Point pos, Background background)
    {
        this.background[pos.getY()][pos.getX()] = background;
    }

    public Optional<Entity> findNearest(Point pos, Class kind)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.getEntities()) {
            if (kind.isInstance(entity)) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    private Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    private int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.getX() - p2.getX();
        int deltaY = p1.getY() - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }

    public boolean parseVein(String[] properties, ImageStore imageStore)
    {
        if (properties.length == VEIN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                                Integer.parseInt(properties[VEIN_ROW]));
            Entity entity = Factory.createVein(properties[VEIN_ID], pt,
                                    Integer.parseInt(
                                            properties[VEIN_ACTION_PERIOD]),
                                    imageStore.getImageList(VEIN_KEY));
            tryAddEntity(entity);
        }

        return properties.length == VEIN_NUM_PROPERTIES;
    }

    public void load(Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                                                     lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                      e.getMessage()));
            }
            lineNumber++;
        }
    }

    public boolean processLine(
            String line, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return parseBackground(properties, imageStore);
                case MINER_KEY:
                    return parseMiner(properties, imageStore);
                case OBSTACLE_KEY:
                    return parseObstacle(properties, imageStore);
                case ORE_KEY:
                    return parseOre(properties, imageStore);
                case SMITH_KEY:
                    return parseSmith(properties, imageStore);
                case VEIN_KEY:
                    return parseVein(properties, imageStore);
            }
        }

        return false;
    }

    private boolean parseBackground(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                                 Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            setBackground(pt,
                          new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    private boolean parseMiner(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == MINER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                                 Integer.parseInt(properties[MINER_ROW]));
            Entity entity = Factory.createMinerNotFull(properties[MINER_ID],
                                               Integer.parseInt(
                                                       properties[MINER_LIMIT]),
                                               pt, Integer.parseInt(
                            properties[MINER_ACTION_PERIOD]), Integer.parseInt(
                            properties[MINER_ANIMATION_PERIOD]),
                                               imageStore.getImageList(MINER_KEY));
            tryAddEntity(entity);
        }

        return properties.length == MINER_NUM_PROPERTIES;
    }

    private boolean parseObstacle(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                                 Integer.parseInt(properties[OBSTACLE_ROW]));
            Obstacle entity = Factory.createObstacle(properties[OBSTACLE_ID], pt,
                                           imageStore.getImageList(OBSTACLE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    private boolean parseOre(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == ORE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                                 Integer.parseInt(properties[ORE_ROW]));
            Entity entity = Factory.createOre(properties[ORE_ID], pt, Integer.parseInt(
                    properties[ORE_ACTION_PERIOD]),
                                      imageStore.getImageList(ORE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == ORE_NUM_PROPERTIES;
    }

    private boolean parseSmith(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == SMITH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                                 Integer.parseInt(properties[SMITH_ROW]));
            BlackSmith entity = Factory.createBlacksmith(properties[SMITH_ID], pt,
                                             imageStore.getImageList(SMITH_KEY));
            tryAddEntity(entity);
        }

        return properties.length == SMITH_NUM_PROPERTIES;
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (withinBounds(pos)) {
            return Optional.of(Background.getCurrentImage(getBackgroundCell(pos)));
        }
        else {
            return Optional.empty();
        }
    }
}
