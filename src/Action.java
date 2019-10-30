public interface Action
{
    Executable getExecutable();
    int getRepeatCount();

    void executeAction(EventScheduler scheduler);
}
