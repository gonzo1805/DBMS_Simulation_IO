package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;


import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Connection extends Module<Query> {

    /**
     * Constructor
     *
     * @param numberOfFreeServers the amount of concurrent connections that the module can handle
     * @param eventList           the event list of the simulation
     * @param simulation          a pointer to the simulation
     */
    public Connection(int numberOfFreeServers, Queue<Event> eventList, Simulation simulation, Generator generator) {
        this.numberOfFreeServers = numberOfFreeServers;
        this.beingServedQueries = new PriorityQueue<Query>();
        this.eventList = eventList;
        this.simulation = simulation;
        this.generator = generator;
    }

    /**
     * Enters a new query on the servers if there are at least one free server, if not, reject the query
     * Also returns the departure time of the query
     * @param query the Query to insert on queue
     * @return the time that the query is going to finish it´s time on the server
     */
    public double entriesANewQuery(Query query) {
        if (numberOfFreeServers > 0) {
            numberOfFreeServers--;
            query.setArrivalTime(simulation.getTime());
            beingServedQueries.add(query);
            query.setDepartureTime(getGenerator().getRandomUniform(0.01, 0.05) + query.getArrivalTime());
            query.setBeingServed(true);
            return query.getDepartureTime();
        } else {
            rejectQuery(query);
            return -1;
        }
    }

    /**
     * Connection doesn´t use this method
     */
    public void aQueryIsServed() {

    }

    /**
     * Set the kill boolean on true of the query
     * @param query the query that we wan to kill
     */
    public void rejectQuery(Query query) {
        query.kill();
    }

    /**
     * Return the query from the beingServedQuery
     * @return the query from the beingServedQuery
     */
    public Query aQueryFinished() {
        numberOfFreeServers++;
        Query finished = beingServedQueries.poll();
        finished.setConectionDuration(simulation.getTime() - finished.getArrivalTime());
        finished.setBeingServed(false);
        return finished;
    }

    /**
     * Look if the query is alive or dead
     * @param query the query that we want to see the kill boolean
     * @return true if the boolean is alive
     */
    public boolean confirmAliveQuery(Query query) {
        return !query.getDead();
    }

    public boolean isAQueryBeingServed(){
        return !beingServedQueries.isEmpty();
    }

    public Query nextQueryToBeOut(){
        return beingServedQueries.peek();
    }
}
