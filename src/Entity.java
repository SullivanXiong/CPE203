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
    int getAnimationPeriod();

    void addEntity(WorldModel world, Entity entity);

    void removeEntity(WorldModel world, Entity entity);

    void removeEntityAt(WorldModel world, Point pos);

    void unscheduleAllEvents(EventScheduler scheduler, Entity target);
}