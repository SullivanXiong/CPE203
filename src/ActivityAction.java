public class ActivityAction implements Action 
{
    // Instance variables
    private ActiveEntity entity;      // Change entity type to something more applicable
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActivityAction(ActiveEntity entity, WorldModel world,
        ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }
    
    
    public void executeAction(EventScheduler scheduler)
    {
        entity.executeActivity(world, imageStore, scheduler);
    }
}