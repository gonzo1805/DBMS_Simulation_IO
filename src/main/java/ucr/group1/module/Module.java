package ucr.group1.module;

import ucr.group1.event.*;
import java.util.Queue;

import ucr.group1.generator.Generator;
import ucr.group1.query.*;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.ModuleStatistics;

/**
 * Created by Gonzalo on 2/8/2017.
 */
public abstract class Module<E> {
    protected Queue<E> queue;
    protected int numberOfServers;
    protected int numberOfFreeServers;
    protected Queue<E> beingServedQueries;
    protected Generator generator;
    protected Simulation simulation;
    protected ModuleStatistics moduleStatistics;
    protected Module<E> nextModule;

    /**
     * Inserts a Query on queue if there is not free servers, if there are free servers, inserts a Query on
     * beignServedQuerys
     * @param query the Query to insert in the module
     * @return the time that the query is going to finish it´s time on the server, it returns -1
     * if there are no free servers and is rejected or put on queue
     */
    public abstract double entriesANewQuery(Query query);

    /**
     * Gets Query and marks it as dead query.
     */
    public abstract void rejectQuery(Query query);

    /**
     * Gets and pull the next Query from beingServedQueries
     * @return the next Query from beingServedQueries
     */
    public abstract Query aQueryFinished();

    public abstract void updateL_sStatistics();

    public abstract void updateL_qStatistics();

    /**
     * @return True if and only if the query was founded in the queue
     */
    public boolean kickAQueryFromQueue(Query query) {
        return queue.remove(query);
    }

    /************************************************ Getters *********************************************************/

    public ModuleStatistics getStatistics(){
        return moduleStatistics;
    }

    /**
     * @return The number of servers of the module
     */
    public int getNumberOfServers() {
        return numberOfServers;
    }

    /**
     * @return The number of free servers
     */
    public int getNumberOfFreeServers() {
        return numberOfFreeServers;
    }

    /**
     * @return The number of queries on queue
     */
    public abstract int getNumberOfQueriesOnQueue();

    /**
     * @return The number of queries that are being served
     */
    public abstract int getNumberOfQueriesBeingServed();

    /**
     * @return The generator of the simulation
     */
    public Generator getGenerator() {
        return generator;
    }

    public int getNumberOfBusyServers() {
        return (numberOfServers - numberOfFreeServers);
    }
}
