package ucr.group1.event;

import ucr.group1.query.Query;

import java.util.Comparator;

/**
 * Created by Gonzalo on 2/11/2017.
 */
public class Event {

    private Query query;
    private double time;
    private eventType eventType;

    public enum eventType {
        ENTER_CONNECTION,
        RETURN_TO_CONNECTION,
        EXIT_CONNECTION,
        ENTER_SYSTEMCALL,
        EXIT_SYSTEMCALL,
        ENTER_VALIDATION,
        EXIT_VALIDATION,
        ENTER_STORAGE,
        EXIT_STORAGE,
        ENTER_EXECUTION,
        EXIT_EXECUTION,
        KILL
    }

    public Query getQuery() {
        return query;
    }

    public double getTime() {
        return time;
    }

    public Event.eventType getEventType() {
        return eventType;
    }

    public Event(eventType event, double time, Query associateQuery) {
        this.eventType = event;
        this.time = time;
        this.query = associateQuery;
    }


}
