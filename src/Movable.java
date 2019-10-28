public interface Movable
{
    Point nextPosition(WorldModel world, Point destPos);

    boolean move(Entity entity, WorldModel world, Entity target, EventScheduler scheduler);

    void moveEntity(WorldModel world, Point pos);
}