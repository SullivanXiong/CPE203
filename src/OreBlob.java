import java.util.Optional;
import java.util.List;

import processing.core.PImage;

public class OreBlob extends MovableEntity
{
    public OreBlob(String id,
        Point position, List<PImage> images,
        int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected void executeActivity(WorldModel world,
            ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(super.getPosition(), Vein.class);
        long nextPeriod = super.getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (move(world, blobTarget.get(), scheduler)) {
                Quake quake = Factory.createQuake(tgtPos,
                                           imageStore.getImageList(Factory.QUAKE_KEY));

                quake.addEntity(world);
                nextPeriod += super.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                      Factory.createActivityAction(this, world, imageStore),
                      nextPeriod);
    }

    public boolean move(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(super.getPosition(), target.getPosition())) {
            target.removeEntity(world);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                super.moveEntity(world, nextPos);
            }
            return false;
        }
    }

    private boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY()
                && Math.abs(p1.getX() - p2.getX()) == 1);
    }

    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz, super.getPosition().getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass().isInstance(Ore.class))))
        {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(super.getPosition().getX(), super.getPosition().getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass().isInstance(Ore.class))))

            {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }
}