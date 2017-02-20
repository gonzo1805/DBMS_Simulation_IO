package ucr.group1.module;

import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;


import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Connection extends Module<Query> {

    private List<Query> queriesExpectedToBeReturned;

    /**
     * Constructor
     *
     * @param numberOfFreeServers   The amount of concurrent connections that the module can handle at once-
     * @param simulation            A pointer to the simulation
     * @param generator
     */
    public Connection(int numberOfFreeServers, Simulation simulation, Generator generator) {
        this.numberOfFreeServers = numberOfFreeServers;
        this.numberOfServers = numberOfFreeServers;
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers, new QueryComparator());
        this.simulation = simulation;
        this.generator = generator;
        this.queriesExpectedToBeReturned = new LinkedList<Query>();
    }

    /**
     * Enters a new query on the servers if there are at least one free server, if not, reject the query
     * Also returns the departure time of the query
     * @param query the Query to insert on queue
     * @return the time that the query is going to finish it´s time on the server, it returns -1
     * if there are no free servers
     */
    public double entriesANewQuery(Query query) {
        if (numberOfFreeServers > 0) {
            numberOfFreeServers--;
            query.setArrivalTime(simulation.getTime());
            queriesExpectedToBeReturned.add(query);
            query.setDepartureTime(getGenerator().getRandomUniform(0.01, 0.05) + query.getArrivalTime());
            query.addLifeSpan(query.getDepartureTime() - query.getArrivalTime());
            simulation.getConnectionStatistics().updateModuleTime(query, query.getDepartureTime() - query.getArrivalTime());
            return query.getDepartureTime();
        } else {
            rejectQuery(query);
            return -1;
        }
    }

    /**
     * Connection doesn't use this method
     */
    public void aQueryIsServed() {}

    public void rejectQuery(Query query) {
        query.kill();
    }

    public Query aQueryFinished() {
        numberOfFreeServers++;
        Query finished = beingServedQueries.poll();
        finished.addLifeSpan(simulation.getTime() - finished.getArrivalTime());
        simulation.getConnectionStatistics().updateModuleTime(finished, simulation.getTime() - finished.getArrivalTime());
        finished.setBeingServed(false);
        return finished;
    }

    /**
     *@param query A query is back to the control of the connection to finish
     */
    public void aQueryHasReturned(Query query) {
        queriesExpectedToBeReturned.remove(query);
        query.setBeingServed(true);
        query.setArrivalTime(simulation.getTime());
        query.setDepartureTime(simulation.getTime() + (query.getChargedBlocks()/6));
        beingServedQueries.add(query);
    }

    public boolean aQueryFromQueueIsNowBeingServed(){
        return !beingServedQueries.isEmpty();
    }

    public Query nextQueryFromQueueToBeOut(){
        return beingServedQueries.peek();
    }

    public int getNumberOfQueriesOnQueue(){
        return 0;
    }

    public int getNumberOfQueriesBeingServed(){
        return beingServedQueries.size();
    }
}
