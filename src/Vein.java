import java.util.Optional;
import java.util.Random;
import java.util.List;

import processing.core.PImage;

public class Vein extends ActiveEntity
{
    private final Random rand = new Random();

    private final String ORE_ID_PREFIX = "ore -- ";
    private final int ORE_CORRUPT_MIN = 20000;
    private final int ORE_CORRUPT_MAX = 30000;

    public Vein(String id, Point position,
        List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore,
        EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent()) {
            Ore ore = Factory.createOre(ORE_ID_PREFIX + super.getId(), openPt.get(),
                                ORE_CORRUPT_MIN + rand.nextInt(
                                        ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                                imageStore.getImageList(Factory.ORE_KEY));
            ore.addEntity(world);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore),
            getActionPeriod());
    }
}