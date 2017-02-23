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

    /**
     * Constructor
     *
     * @param event           The event type
     * @param time            The time at which the event occurs
     * @param associatedQuery A pointer to the query associated to the event
     */
    public Event(eventType event, double time, Query associatedQuery) {
        this.eventType = event;
        this.time = time;
        this.query = associatedQuery;
    }


    /********************************************** GETTERS ***********************************************************/

    /**
     * @return the query associated to the event
     */
    public Query getQuery() {
        return query;
    }

    /**
     * @return the time at which the event occurs
     */
    public double getTime() {
        return time;
    }

    /**
     * @return the event type
     */
    public eventType getEventType() {
        return eventType;
    }
}
