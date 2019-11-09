import java.util.List;

import processing.core.PImage;

public abstract class ActiveEntity extends Entity
{
    private int actionPeriod;

    public ActiveEntity(String id, Point position,
        List<PImage> images, int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }


    public int getActionPeriod() { return this.actionPeriod;}
    
    public void scheduleActions(EventScheduler scheduler,
        WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
            Factory.createActivityAction(this, world, imageStore),
            this.getActionPeriod());
    }


    protected abstract void executeActivity (WorldModel world,
        ImageStore imageStore, EventScheduler scheduler);
}