public class ActivityAction implements Action 
{
    // Instance variables
    private Entity entity;      // Change entity type to something more applicable
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActivityAction(Entity entity, WorldModel world,
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
        if (this.entity instanceof MinerFull) {
            this.entity.executeActivity(this.world, this.imageStore, scheduler);
        }
        else if (this.entity instanceof MinerNotFull) {
            this.entity.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else if (this.entity instanceof Ore) {
            this.entity.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else if (this.entity instanceof OreBlob) {
            this.entity.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else if (this.entity instanceof Quake) {
            this.entity.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else if (this.entity instanceof Vein) {
            this.entity.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else {
            throw new UnsupportedOperationException(String.format(
                    "executeActivityAction not supported for %s",
                    this.entity.getClass()));
        }
    }
}

    // public void executeAction(EventScheduler scheduler)
    // {
    //     switch (this.entity.getClass()) {
    //         if (this.entity.getClass() instanceof MinerFull.class)
    //             this.entity.executeActivity(getWorld(),
    //                                      getImageStore(), scheduler);
    //             break;

    //         case MinerNotFull.class:
    //             this.entity.executeActivity(getWorld(),
    //                                         getImageStore(), scheduler);
    //             break;

    //         case Ore.class:
    //             this.entity.executeActivity(getWorld(),
    //                                getImageStore(), scheduler);
    //             break;

    //         case OreBlob.class:
    //             this.entity.executeActivity(getWorld(),
    //                                    getImageStore(), scheduler);
    //             break;

    //         case Quake.class:
    //             this.entity.executeActivity(getWorld(),
    //                                  getImageStore(), scheduler);
    //             break;

    //         case Vein.class:
    //             this.entity.executeActivity(getWorld(),
    //                                 getImageStore(), scheduler);
    //             break;

    //         default:
    //             throw new UnsupportedOperationException(String.format(
    //                     "executeActivityAction not supported for %s",
    //                     this.entity.getClass()));
    //     }
    // }