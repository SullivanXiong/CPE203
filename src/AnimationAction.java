public class AnimationAction implements Action
{
    // Instance variables
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationAction(Entity entity, WorldModel world,
        ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public Entity getEntity() { return this.entity;}
    public int getRepeatCount() { return this.repeatCount;}

    public void executeAction(EventScheduler scheduler)
    {
        this.imageStore.nextImage(this.entity);

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                          this.entity.createAnimationAction(Math.max(this.repeatCount - 1, 0), this.imageStore),
                          this.entity.getAnimationPeriod());
        }
    }
}