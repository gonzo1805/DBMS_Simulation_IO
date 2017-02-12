package ucr.group1.module;

import ucr.group1.event.*;
import java.util.List;
import java.util.Queue;

import ucr.group1.generator.Generator;
import ucr.group1.query.*;
import ucr.group1.simulation.Simulation;

/**
 * Created by Gonzalo on 2/8/2017.
 */
public abstract class Module<E> {
    protected Queue<E> queue;
    //protected int numberOfServers;
    protected int numberOfFreeServers;
    protected Queue<E> beingServedQueries;
    protected Queue<Event> eventList;
    protected Generator generator;
    protected Simulation simulation;

    /*public Module(Generator generator) {
        this.generator = generator;
    }*/

    /**
     * Inserts a Query on queue if there is not free servers, if there are free servers, inserts a Query on
     * beignServedQuerys
     * @param query the Query to insert on queue
     */
    public abstract double entriesANewQuery(Query query);

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

    /**
     * Return a boolean if the query needs to be rejected on the next module or get out from
     * a queue while waiting
     * @param query the query that we want to see the kill boolean
     * @return the kill boolean
     */
    public abstract boolean confirmAliveQuery(Query query);


    /**
     * Setters and Getters
     */


    /**
     *
     * @return
     */
    public Queue<E> getQueue() {
        return queue;
    }

    /*public int getNumberOfServers() {
        return numberOfServers;
    }*/

    public int getNumberOfFreeServers() {
        return numberOfFreeServers;
    }

    public Queue<E> getBeingServedQueries() {
        return beingServedQueries;
    }

    public Queue<Event> getEventList() {
        return eventList;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setQueue(Queue<E> queue) {
        this.queue = queue;
    }

    /*public void setNumberOfServers(int numberOfServers) {
        this.numberOfServers = numberOfServers;
    }*/

    public void setNumberOfFreeServers(int numberOfFreeServers) {
        this.numberOfFreeServers = numberOfFreeServers;
    }

    public void setBeingServedQueries(Queue<E> beingServedQueries) {
        this.beingServedQueries = beingServedQueries;
    }

    /*public void setEventList(Queue<Event> eventList) {
        this.eventList = eventList;
    }*/

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }
}
