import java.util.Optional;
import java.util.Random;
import java.util.List;

import processing.core.PImage;

public class Vein implements Executable
{
    // Instance Variables.
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    private final Random rand = new Random();

    private final String ORE_ID_PREFIX = "ore -- ";
    private final int ORE_CORRUPT_MIN = 20000;
    private final int ORE_CORRUPT_MAX = 30000;

    public Vein(String id,
        Point position, List<PImage> images,
        int resourceLimit, int resourceCount,
        int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public String getId() { return this.id ; }
    public Point getPosition() { return this.position; }
    public void setPosition(Point point) { this.position = point; }
    public List<PImage> getImages() { return this.images; }
    public int getImageIndex() { return this.imageIndex; }
    public void setImageIndex(int imageIndex) { this.imageIndex = imageIndex; }
    public int getResourceLimit() { return this.resourceLimit; }
    public int getResourceCount() { return this.resourceCount; }
    public int getActionPeriod() { return this.actionPeriod; }

    public void executeActivity(WorldModel world, ImageStore imageStore,
        EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent()) {
            Ore ore = Factory.createOre(ORE_ID_PREFIX + getId(), openPt.get(),
                                ORE_CORRUPT_MIN + rand.nextInt(
                                        ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                                imageStore.getImageList(Factory.ORE_KEY));
            addEntity(world, ore);
            scheduleActions(ore, scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore),
            getActionPeriod());
    }
    
    public ActivityAction createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction((Executable)this, world, imageStore, 0);
    }

    public AnimationAction createAnimationAction(int repeatCount, ImageStore imageStore) {
        return new AnimationAction((Executable) this, null, imageStore,
                            repeatCount);
    }
    
    public int getAnimationPeriod() {
        throw new UnsupportedOperationException(
            String.format("getAnimationPeriod not supported for %s",
                          this.getClass()));
    }

    public void scheduleActions(Entity entity, EventScheduler scheduler,
        WorldModel world, ImageStore imageStore)
    {
        Executable executable = (Executable) entity;
        scheduler.scheduleEvent(executable,
            executable.createActivityAction(world, imageStore),
            executable.getActionPeriod());
    }

    public void addEntity(WorldModel world, Entity entity) {
        if (world.withinBounds(entity.getPosition())) {
            world.setOccupancyCell(entity.getPosition(), entity);
            world.getEntities().add(entity);
        }
    }

    public void removeEntity(WorldModel world, Entity entity) {
        removeEntityAt(world, entity.getPosition());
    }

    public void removeEntityAt(WorldModel world, Point pos) {
        if (world.withinBounds(pos) && world.getOccupancyCell(pos) != null) {
            Entity entity = world.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            world.getEntities().remove(entity);
            world.setOccupancyCell(pos, null);
        }
    }

    public void unscheduleAllEvents(EventScheduler scheduler, Entity target)
    {
        List<Event> pending = scheduler.getPendingEvents().remove(target);

        if (pending != null) {
            for (Event event : pending) {
                scheduler.getEventQueue().remove(event);
            }
        }
    }
}