package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.ModuleStatistics;


import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import static ucr.group1.event.EventType.*;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class ClientManagementModule extends Module<Query> {

    private List<Query> queriesExpectedToBeReturned;

    /**
     * Constructor
     *
     * @param numberOfFreeServers The amount of concurrent connections that the module can handle at once-
     * @param simulation          A pointer to the simulation
     * @param generator
     */
    public ClientManagementModule(int numberOfFreeServers, Simulation simulation, Generator generator) {
        this.numberOfFreeServers = numberOfFreeServers;
        this.numberOfServers = numberOfFreeServers;
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers, new QueryComparator());
        this.simulation = simulation;
        this.generator = generator;
        this.queriesExpectedToBeReturned = new LinkedList<Query>();
        this.moduleStatistics = new ModuleStatistics(this, this.simulation);
    }

    /**
     * Enters a new query on the servers if there are at least one free server, if not, reject the query
     * Also returns the departure time of the query
     *
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
            moduleStatistics.updateModuleTime(query, query.getDepartureTime() - query.getArrivalTime());
            return query.getDepartureTime();
        } else {
            rejectQuery(query);
            return -1;
        }
    }

    /**
     * ClientManagementModule doesn't use this method
     */
    public void aQueryIsServed() {
    }

    public void rejectQuery(Query query) {
        query.kill();
    }

    public Query aQueryFinished() {
        numberOfFreeServers++;
        Query finished = beingServedQueries.poll();
        finished.addLifeSpan(simulation.getTime() - finished.getArrivalTime());
        moduleStatistics.updateModuleTime(finished, simulation.getTime() - finished.getArrivalTime());
        finished.setBeingServed(false);
        return finished;
    }

    /**
     * @param query A query is back to the control of the connection to finish
     */
    public void aQueryHasReturned(Query query) {
        queriesExpectedToBeReturned.remove(query);
        query.setBeingServed(true);
        query.setArrivalTime(simulation.getTime());
        query.setDepartureTime(simulation.getTime() + (query.getChargedBlocks() / 6));
        beingServedQueries.add(query);
    }

    public boolean aQueryFromQueueIsNowBeingServed() {
        return !beingServedQueries.isEmpty();
    }

    public Query nextQueryFromQueueToBeOut() {
        return beingServedQueries.peek();
    }

    public int getNumberOfQueriesOnQueue() {
        return 0;
    }

    public int getNumberOfQueriesBeingServed() {
        return beingServedQueries.size();
    }

    public void releaseAServer() {
        numberOfFreeServers++;
    }

    public void enterConnectionEvent(int idAsigner, Event actualEvent) {
        simulation.setTime(actualEvent.getTime());
        simulation.addLineInTimeLog("A new query is trying to get a connection");
        double exitTime = entriesANewQuery(actualEvent.getQuery());
        if (exitTime > -1) {
            moduleStatistics.updateTimeBetweenArrives(simulation.getTime());
            simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                    " is connected with the database");
            Event nextEvent = new Event(ENTER_SYSTEMCALL, exitTime, actualEvent.getQuery());
            actualEvent.getQuery().setNextEvent(nextEvent);
            simulation.addEvent(nextEvent);
            Event killEvent = new Event(KILL, simulation.getTimeOut() + actualEvent.getTime(), actualEvent.getQuery());
            actualEvent.getQuery().setKillEvent(killEvent);
            simulation.addEvent(killEvent);
        } else {
            simulation.addLineInTimeLog("The system reached the maximum of simultaneous " +
                    "connections, the new query is rejected");
            simulation.getQueryStatistics().rejectAQuery();
        }
        simulation.addEvent(new Event(ENTER_CONNECTION, simulation.getTime() + simulation.getGenerator().getExponential(1.7142),
                new Query(idAsigner++, simulation.getGenerator())));
        simulation.finalizeEvent(actualEvent);
    }

    public void returnToConnectionEvent(Event actualEvent) {
        simulation.setTime(actualEvent.getTime());
        aQueryHasReturned(actualEvent.getQuery());
        simulation.addEvent(new Event(EXIT_CONNECTION, actualEvent.getQuery().getDepartureTime(), actualEvent.getQuery()));
        simulation.finalizeEvent(actualEvent);
    }

    public void exitConnectionEvent(Event actualEvent) {
        simulation.setTime(actualEvent.getTime());
        Query fromModule = aQueryFinished();
        simulation.addLineInTimeLog(simulation.getTimeInHHMMSS() + "The query " + fromModule.getId() + " is out from connection.");
        simulation.thisQueryKillNeverHappened(fromModule);
        simulation.getQueryStatistics().addFinishedQuery(fromModule);
        simulation.finalizeEvent(actualEvent);
    }

    public void updateL_sStatistics() {
        moduleStatistics.updateL_S(beingServedQueries.size());
    }

    public void updateL_qStatistics() {
        // Nothing happens because this module hasn't a queue
    }
}
