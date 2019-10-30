public class ActivityAction implements Action 
{
    // Instance variables
    private Executable executable;      // Change entity type to something more applicable
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActivityAction(Executable executable, WorldModel world,
        ImageStore imageStore, int repeatCount)
    {
        this.executable = executable;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public Executable getExecutable() { return this.executable;}
    public int getRepeatCount() { return this.repeatCount;}

    public void executeAction(EventScheduler scheduler)
    {
        if (this.executable instanceof MinerFull) {
            this.executable.executeActivity(this.world, this.imageStore, scheduler);
        }
        else if (this.executable instanceof MinerNotFull) {
            this.executable.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else if (this.executable instanceof Ore) {
            this.executable.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else if (this.executable instanceof OreBlob) {
            this.executable.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else if (this.executable instanceof Quake) {
            this.executable.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else if (this.executable instanceof Vein) {
            this.executable.executeActivity(this.world, this.imageStore, scheduler);
        } 
        else {
            throw new UnsupportedOperationException(String.format(
                    "executeActivityAction not supported for %s",
                    this.executable.getClass()));
        }
    }
}