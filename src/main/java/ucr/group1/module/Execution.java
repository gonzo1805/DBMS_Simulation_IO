package ucr.group1.module;

import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Execution extends Module<Query> {

    Query ddlToBeExecuted;

    public Execution(int numberOfFreeServers, Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = numberOfFreeServers;
        this.queue = new PriorityQueue<Query>(1000000 , new ExecutionComparator());
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers , new QueryComparator());
    }

    public double entriesANewQuery(Query query) {
        if (numberOfFreeServers > 0) {
            numberOfFreeServers--;
            query.setArrivalTime(simulation.getTime());
            beingServedQueries.add(query);
            query.setDepartureTime(getGenerator().getRandomUniform(0.01, 0.05) + query.getArrivalTime());
            return query.getDepartureTime();
        } else {
            rejectQuery(query);
            return -1;
        }
    }

    public void aQueryIsServed() {

    }

    public void rejectQuery(Query query) {

    }

    //public void

    public Query aQueryFinished() {
        return null;
    }

    public boolean confirmAliveQuery(Query query) {
        return false;
    }
}
