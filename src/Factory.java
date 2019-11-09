import java.util.List;
import java.util.Scanner;

import processing.core.PImage;
import processing.core.PApplet;

public class Factory
{
    public static final int PROPERTY_KEY = 0;
    public static final String BLOB_KEY = "blob";
    public static final String QUAKE_KEY = "quake";
    public static final String MINER_KEY = "miner";
    public static final String ORE_KEY = "ore";
    public static final String VEIN_KEY = "vein";
    public static final String BGND_KEY = "background";
    public static final String OBSTACLE_KEY = "obstacle";
    public static final String SMITH_KEY = "blacksmith";

    public static final int MINER_NUM_PROPERTIES = 7;
    public static final int ORE_NUM_PROPERTIES = 5;
    public static final int VEIN_NUM_PROPERTIES = 5;

    public static final int MINER_ID = 1;
    public static final int MINER_COL = 2;
    public static final int MINER_ROW = 3;
    public static final int MINER_LIMIT = 4;
    public static final int MINER_ACTION_PERIOD = 5;
    public static final int MINER_ANIMATION_PERIOD = 6;

    public static final int ORE_ID = 1;
    public static final int ORE_COL = 2;
    public static final int ORE_ROW = 3;
    public static final int ORE_ACTION_PERIOD = 4;
    public static final int ORE_REACH = 1;

    public static final int VEIN_ID = 1;
    public static final int VEIN_COL = 2;
    public static final int VEIN_ROW = 3;
    public static final int VEIN_ACTION_PERIOD = 4;

    public static final int BGND_ID = 1;
    public static final int BGND_COL = 2;
    public static final int BGND_ROW = 3;

    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;

    public static final int SMITH_ID = 1;
    public static final int SMITH_COL = 2;
    public static final int SMITH_ROW = 3;

    public static final int BGND_NUM_PROPERTIES = 4;
    public static final int OBSTACLE_NUM_PROPERTIES = 4;
    public static final int SMITH_NUM_PROPERTIES = 4;

    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;

    public static final int COLOR_MASK = 0xffffff;

    public static BlackSmith createBlacksmith(
        String id, Point position, List<PImage> images)
    {
        return new BlackSmith(id, position, images);
    }

    public static MinerFull createMinerFull(
        String id,
        int resourceLimit,
        Point position,
        int actionPeriod,
        int animationPeriod,
        List<PImage> images)
    {
        return new MinerFull(id, position, images,
            resourceLimit, resourceLimit, actionPeriod,
            animationPeriod);
    }

    public static MinerNotFull createMinerNotFull(
        String id,
        int resourceLimit,
        Point position,
        int actionPeriod,
        int animationPeriod,
        List<PImage> images)
    {
        return new MinerNotFull(id, position, images,
            resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public static Obstacle createObstacle(
        String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }

    public static Ore createOre(
        String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Ore(id, position, images, actionPeriod);
    }

    public static OreBlob createOreBlob(
        String id,
        Point position,
        int actionPeriod,
        int animationPeriod,
        List<PImage> images)
    {
        return new OreBlob(id, position, images, actionPeriod, animationPeriod);
    }

    public static Quake createQuake(
        Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public static Vein createVein(
        String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Vein(id, position, images, actionPeriod);
    }

    public static void loadImages(Scanner in, ImageStore imageStore, PApplet screen)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                imageStore.processImageLine(imageStore.getImages(), in.nextLine(), screen);
            }
            catch (NumberFormatException e) {
                System.out.println(
                        String.format("Image format error on line %d",
                                      lineNumber));
            }
            lineNumber++;
        }
    }

    public static void setAlpha(PImage img, int maskColor, int alpha) {
        int alphaValue = alpha << 24;
        int nonAlpha = maskColor & COLOR_MASK;
        img.format = PApplet.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
                img.pixels[i] = alphaValue | nonAlpha;
            }
        }
        img.updatePixels();
    }

    public boolean parseBackground(WorldModel world, 
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                                 Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground(pt,
                          new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    public boolean parseMiner(WorldModel world, 
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == MINER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                                 Integer.parseInt(properties[MINER_ROW]));
            Entity entity = createMinerNotFull(properties[MINER_ID],
                                               Integer.parseInt(
                                                       properties[MINER_LIMIT]),
                                               pt, Integer.parseInt(
                            properties[MINER_ACTION_PERIOD]), Integer.parseInt(
                            properties[MINER_ANIMATION_PERIOD]),
                                               imageStore.getImageList(MINER_KEY));
            tryAddEntity(world, entity);
        }

        return properties.length == MINER_NUM_PROPERTIES;
    }

    public boolean parseObstacle(WorldModel world, 
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                                 Integer.parseInt(properties[OBSTACLE_ROW]));
            Obstacle entity = createObstacle(properties[OBSTACLE_ID], pt,
                                           imageStore.getImageList(OBSTACLE_KEY));
            tryAddEntity(world, entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public boolean parseOre(WorldModel world, 
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == ORE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                                 Integer.parseInt(properties[ORE_ROW]));
            Entity entity = createOre(properties[ORE_ID], pt, Integer.parseInt(
                    properties[ORE_ACTION_PERIOD]),
                                      imageStore.getImageList(ORE_KEY));
            tryAddEntity(world, entity);
        }

        return properties.length == ORE_NUM_PROPERTIES;
    }

    public boolean parseSmith(WorldModel world, 
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == SMITH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                                 Integer.parseInt(properties[SMITH_ROW]));
            BlackSmith entity = createBlacksmith(properties[SMITH_ID], pt,
                                             imageStore.getImageList(SMITH_KEY));
            tryAddEntity(world, entity);
        }

        return properties.length == SMITH_NUM_PROPERTIES;
    }
    
    public boolean parseVein(WorldModel world, String[] properties, ImageStore imageStore)
    {
        if (properties.length == VEIN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                                Integer.parseInt(properties[VEIN_ROW]));
            Entity entity = createVein(properties[VEIN_ID], pt,
                                    Integer.parseInt(
                                            properties[VEIN_ACTION_PERIOD]),
                                    imageStore.getImageList(VEIN_KEY));
            tryAddEntity(world, entity);
        }

        return properties.length == VEIN_NUM_PROPERTIES;
    }

    public void tryAddEntity(WorldModel world, Entity entity) {
        if (world.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        entity.addEntity(world);
    }

    public static ActivityAction createActivityAction(ActiveEntity entity, WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction(entity, world, imageStore, 0);
    }

    public static AnimationAction createAnimationAction(AnimatedEntity entity, int repeatCount, ImageStore imageStore) {
        return new AnimationAction(entity, null, imageStore,
                            repeatCount);
    }
}