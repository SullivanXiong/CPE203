import java.util.List;

import processing.core.PImage;

public abstract class MinerEntity extends MovableEntity
{
    private int resourceLimit;
    private int resourceCount;

    public MinerEntity(String id,
        Point position, List<PImage> images,
        int resourceLimit, int resourceCount,
        int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }


    public int getResourceLimit() { return this.resourceLimit; }

    public int getResourceCount() { return this.resourceCount; }

    public void incrementResourceCount() { this.resourceCount++; }
}