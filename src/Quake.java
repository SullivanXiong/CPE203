import java.util.List;
import java.util.Optional;
import processing.core.PImage;

public class Quake implements Executable
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

    private final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(String id,
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
        unscheduleAllEvents(scheduler, this);
        removeEntity(world, this);
    }
    
    public ActivityAction createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction((Executable) this, world, imageStore, 0);
    }

    public AnimationAction createAnimationAction(int repeatCount, ImageStore imageStore) {
        return new AnimationAction((Executable) this, null, imageStore,
                            repeatCount);
    }
    
    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void scheduleActions(Entity entity, EventScheduler scheduler,
        WorldModel world, ImageStore imageStore)
    {
        Executable executable = (Executable) entity;
        scheduler.scheduleEvent(executable,
            executable.createActivityAction(world, imageStore),
            executable.getActionPeriod());
        scheduler.scheduleEvent(executable,
            executable.createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT, imageStore),
            executable.getAnimationPeriod());
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

    public void addEntity(WorldModel world, Entity entity) {;}

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