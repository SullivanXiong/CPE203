import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Optional;

public final class EventScheduler
{
    private PriorityQueue<Event> eventQueue;
    private Map<Entity, List<Event>> pendingEvents;
    private double timeScale;

    public EventScheduler(double timeScale) {
        this.eventQueue = new PriorityQueue<>(new EventComparator());
        this.pendingEvents = new HashMap<>();
        this.timeScale = timeScale;
    }

    public PriorityQueue<Event> getEventQueue() { return this.eventQueue;}
    public Map<Entity, List<Event>> getPendingEvents() { return this.pendingEvents;}
    public double getTimeScale() { return this.timeScale;}

    public void scheduleEvent(
            Entity entity,
            Action action,
            long afterPeriod)
    {
        long time = System.currentTimeMillis() + (long)(afterPeriod
                * this.timeScale);
        Event event = new Event(action, time, entity);

        this.eventQueue.add(event);

        // update list of pending events for the given entity
        List<Event> pending = this.pendingEvents.getOrDefault(entity,
                                                                   new LinkedList<>());
        pending.add(event);
        this.pendingEvents.put(entity, pending);
    }

    public void removePendingEvent(Event event)
    {
        List<Event> pending = this.pendingEvents.get(event.getEntity());

        if (pending != null) {
            pending.remove(event);
        }
    }

    public void updateOnTime(long time) {
        while (!this.eventQueue.isEmpty()
                && this.eventQueue.peek().getTime() < time) {
            Event next = this.eventQueue.poll();

            removePendingEvent(next);

            next.getAction().executeAction(this);
        }
    }

    public void unscheduleAllEvents(Entity target)
    {
        List<Event> pending = getPendingEvents().remove(target);

        if (pending != null) {
            for (Event event : pending) {
                getEventQueue().remove(event);
            }
        }
    }
}
