import java.util.List;

import processing.core.PImage;

public abstract class Entity
{   
    // Instance Variables.
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id,
        Point position, List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public String getId() { return this.id ; }
    public Point getPosition() { return this.position; }
    public void setPosition(Point point) { this.position = point; }
    public List<PImage> getImages() { return this.images; }
    public int getImageIndex() { return this.imageIndex; }
    public void setImageIndex(int imageIndex) { this.imageIndex = imageIndex; }
    
    public void addEntity(WorldModel world) {
        if (world.withinBounds(this.getPosition())) {
            world.setOccupancyCell(this.getPosition(), this);
            world.getEntities().add(this);
        }
    }

    public void removeEntity(WorldModel world) {
        removeEntityAt(world, this.position);
    }

    public void removeEntityAt(WorldModel world, Point pos) {
        if (world.withinBounds(pos) && world.getOccupancyCell(pos) != null) {
            Entity entity = world.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            world.getEntities().remove(entity);
            world.setOccupancyCell(pos, null);
        }
    }

    public void moveEntity(WorldModel world, Point pos) {
        Point oldPos = this.getPosition();
        if (world.withinBounds(pos) && !pos.equals(oldPos)) {
            world.setOccupancyCell(oldPos, null);
            removeEntityAt(world, pos);
            world.setOccupancyCell(pos, this);
            this.setPosition(pos);
        }
    }

    public PImage getCurrentImage() {
        return this.getImages().get(this.imageIndex);
    }
}