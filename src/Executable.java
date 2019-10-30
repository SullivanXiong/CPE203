public interface Executable extends Entity
{
    void executeActivity (WorldModel world,
    ImageStore imageStore, EventScheduler scheduler);
    
    ActivityAction createActivityAction(WorldModel world,
        ImageStore imageStore);

    AnimationAction createAnimationAction(int repeatCount, ImageStore imageStore);

    void scheduleActions(Entity entity, EventScheduler scheduler,
        WorldModel world, ImageStore imageStore);
}