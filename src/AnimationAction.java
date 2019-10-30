public class AnimationAction implements Action
{
    // Instance variables
    private Executable executable;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationAction(Executable executable, WorldModel world,
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
        this.imageStore.nextImage(this.executable);

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.executable,
                          this.executable.createAnimationAction(Math.max(this.repeatCount - 1, 0), this.imageStore),
                          this.executable.getAnimationPeriod());
        }
    }
}