import java.util.Random;
import java.util.List;

import processing.core.PImage;

public class Ore extends ActiveEntity
{

    private final Random rand = new Random();

    private final String BLOB_ID_SUFFIX = " -- blob";
    private final int BLOB_PERIOD_SCALE = 4;
    private final int BLOB_ANIMATION_MIN = 50;
    private final int BLOB_ANIMATION_MAX = 150;

    public Ore(String id, Point position,
        List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore,
        EventScheduler scheduler)
    {
        Point pos = this.getPosition();

        super.removeEntity(world);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = Factory.createOreBlob(super.getId() + BLOB_ID_SUFFIX, pos,
                                    super.getActionPeriod() / BLOB_PERIOD_SCALE,
                                    BLOB_ANIMATION_MIN + rand.nextInt(
                                            BLOB_ANIMATION_MAX
                                                    - BLOB_ANIMATION_MIN),
                                    imageStore.getImageList(Factory.BLOB_KEY));

        blob.addEntity(world);
        blob.scheduleActions(scheduler, world, imageStore);
    }
}