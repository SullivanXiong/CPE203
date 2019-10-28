import java.util.Optional;
import java.util.List;

import processing.core.PImage;

public class OreBlob implements Entity, Movable
{
    // Instance Variables
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public OreBlob(String id,
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

    public void executeActivity(WorldModel world,
            ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(this.getPosition(), Vein.class);
        long nextPeriod = this.getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (move(this, world, blobTarget.get(), scheduler)) {
                Entity quake = Factory.createQuake(tgtPos,
                                           imageStore.getImageList(world.getQUAKE_KEY()));

                addEntity(world, quake);
                nextPeriod += getActionPeriod();
                scheduleActions(quake, scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                      createActivityAction(world, imageStore),
                      nextPeriod);
    }

    public boolean move(Entity entity, WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(this.position, target.getPosition())) {
            removeEntity(world, target);
            unscheduleAllEvents(scheduler, target);
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    unscheduleAllEvents(scheduler, occupant.get());
                }

                moveEntity(world, nextPos);
            }
            return false;
        }
    }

    private boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY()
                && Math.abs(p1.getX() - p2.getX()) == 1);
    }

    public void moveEntity(WorldModel world, Point pos) {
        Point oldPos = this.position;
        if (world.withinBounds(pos) && !pos.equals(oldPos)) {
            world.setOccupancyCell(oldPos, null);
            removeEntityAt(world, pos);
            world.setOccupancyCell(pos, this);
            this.position = pos;
        }
    }

    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass().isInstance(Ore.class))))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass().isInstance(Ore.class))))

            {
                newPos = this.position;
            }
        }

        return newPos;
    }
    
    public ActivityAction createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction(this, world, imageStore, 0);
    }

    public AnimationAction createAnimationAction(int repeatCount, ImageStore imageStore) {
        return new AnimationAction(this, null, imageStore,
                            repeatCount);
    }
    
    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void scheduleActions(Entity entity, EventScheduler scheduler,
        WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(entity,
            entity.createActivityAction(world, imageStore),
            entity.getActionPeriod());
        scheduler.scheduleEvent(entity,
            entity.createAnimationAction(0, imageStore),
            entity.getAnimationPeriod());
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