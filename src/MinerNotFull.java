import java.util.Optional;
import java.util.List;

import processing.core.PImage;

public class MinerNotFull extends MinerEntity
{
    public MinerNotFull(String id,
        Point position, List<PImage> images,
        int resourceLimit, int resourceCount,
        int actionPeriod, int animationPeriod)
    {
        super(id, position, images, resourceLimit,
            resourceCount, actionPeriod, animationPeriod);
    }

    protected void executeActivity(WorldModel world,
        ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(super.getPosition(), Ore.class);

        if (!notFullTarget.isPresent() || !move(world, notFullTarget.get(), scheduler)
                || !transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
        }
    }

    protected boolean move(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(super.getPosition(), target.getPosition())) {
            super.incrementResourceCount();
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

    protected Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz, super.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(super.getPosition().getX(), super.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

    public boolean transform(WorldModel world, EventScheduler scheduler,
        ImageStore imageStore)
    {
        if (super.getResourceCount() >= super.getResourceLimit()) {
            MinerFull miner = Factory.createMinerFull(super.getId(), super.getResourceLimit(),
                                        super.getPosition(), super.getActionPeriod(),
                                        super.getAnimationPeriod(),
                                        super.getImages());

            super.removeEntity(world);
            scheduler.unscheduleAllEvents(this);

            miner.addEntity(world);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }
}