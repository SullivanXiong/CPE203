public class AnimationAction implements Action
{
    // Instance variables
    private AnimatedEntity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationAction(AnimatedEntity entity, WorldModel world,
        ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity,
                          Factory.createAnimationAction(entity, Math.max(repeatCount - 1, 0), imageStore),
                          entity.getAnimationPeriod());
        }
    }
}