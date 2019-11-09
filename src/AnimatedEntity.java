import java.util.List;

import processing.core.PImage;

public abstract class AnimatedEntity extends ActiveEntity
{
    private int animationPeriod;

    public AnimatedEntity(String id,
        Point position, List<PImage> images,
        int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }


    public int getAnimationPeriod() { return this.animationPeriod;}

    public void nextImage() {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }

    public void scheduleActions(EventScheduler scheduler,
        WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
            Factory.createActivityAction(this, world, imageStore),
            this.getActionPeriod());
        scheduler.scheduleEvent(this,
            Factory.createAnimationAction(this, 0, imageStore),
            this.getAnimationPeriod());
    }
}