package Results;

import Models.Event;

import java.util.Arrays;
import java.util.Objects;

public class EventResult {
    public EventResult(Event[] events) {
        this.events = events;
        this.event = null;
        this.message = null;
    }

    public EventResult(Event event) {
        this.events = null;
        this.event = event;
        this.message = null;
    }

    public EventResult(String message) {
        this.events = null;
        this.event = null;
        this.message = message;
    }

    private Event[] events;
    private Event event;
    private String message;

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventResult)) return false;
        EventResult that = (EventResult) o;
        return Arrays.equals(getEvents(), that.getEvents()) &&
                Objects.equals(getEvent(), that.getEvent()) &&
                Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getEvent(), getMessage());
        result = 31 * result + Arrays.hashCode(getEvents());
        return result;
    }
}
