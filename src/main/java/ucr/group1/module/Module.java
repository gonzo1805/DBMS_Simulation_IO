package ucr.group1.module;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import ucr.group1.query.*;

/**
 * Created by Gonzalo on 2/8/2017.
 */
public abstract class Module<E> {
    protected Queue<E> queue;
    protected int numberOfServers;
    protected int numberOfFreeServers;
    protected Queue<E> beignServedQuerys;
    protected Queue<Event/*TODO nuestra clase event*/> eventList;

    public Module(int numberOfFreeServers, int numberOfServers, Queue<E> queue, Queue<E> beignServedQuerys, Queue<Event> eventList) {
        this.beignServedQuerys = beignServedQuerys;
        this.queue = queue;
        this.numberOfFreeServers = numberOfFreeServers;
        this.numberOfServers = numberOfServers;
        this.eventList = eventList;
    }

    /**
     * Inserts a Query on queue if there is not free servers, if there are free servers, inserts a Query on
     * beignServedQuerys
     * @param query the Query to insert on queue
     */
    public abstract void entriesANewQuery(Query query);

    /**
     * Gets the next Query and inserts on beignServedQuerys
     */
    public abstract void aQueryIsServed();

    /**
     * Gets Query and pull out from the system
     */
    public abstract void rejectQuery(Query query);

    /**
     * Gets and pull the next Query from beingServedQueries
     * @return the next Query from beingServedQueries
     */
    public abstract Query aQueryFinished();

    public Queue<E> getQueue() {
        return queue;
    }

    public int getNumberOfServers() {
        return numberOfServers;
    }

    public int getNumberOfFreeServers() {
        return numberOfFreeServers;
    }

    public Queue<E> getBeignServedQuerys() {
        return beignServedQuerys;
    }

    public Queue<Event> getEventList() {
        return eventList;
    }
}
