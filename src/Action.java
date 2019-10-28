public interface Action
{
    Entity getEntity();
    int getRepeatCount();

    void executeAction(EventScheduler scheduler);
}
