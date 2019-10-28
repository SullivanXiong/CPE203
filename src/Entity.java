import java.util.List;

import processing.core.PImage;

public interface Entity
{   
    // Inteface Methods

    String getId();
    Point getPosition();
    void setPosition(Point point);
    List<PImage> getImages();
    int getImageIndex();
    void setImageIndex(int imageIndex);
    int getResourceLimit();
    int getResourceCount();
    int getActionPeriod();

    void executeActivity (WorldModel world,
        ImageStore imageStore, EventScheduler scheduler);
        
    ActivityAction createActivityAction(WorldModel world,
        ImageStore imageStore);

    AnimationAction createAnimationAction(int repeatCount, ImageStore imageStore);

    int getAnimationPeriod();

    void scheduleActions(Entity entity, EventScheduler scheduler,
        WorldModel world, ImageStore imageStore);

    void addEntity(WorldModel world, Entity entity);

    void removeEntity(WorldModel world, Entity entity);

    void removeEntityAt(WorldModel world, Point pos);

    void unscheduleAllEvents(EventScheduler scheduler, Entity target);
}