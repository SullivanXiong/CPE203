import java.util.List;

import processing.core.PImage;

public abstract class MovableEntity extends AnimatedEntity
{
    public MovableEntity(String id,
        Point position, List<PImage> images,
        int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected abstract boolean move(WorldModel world, Entity target, EventScheduler scheduler);

    protected abstract Point nextPosition(WorldModel world, Point destPos);
}