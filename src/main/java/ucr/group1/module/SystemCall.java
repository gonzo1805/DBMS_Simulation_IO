package ucr.group1.module;

import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class SystemCall extends Module<Query> {

    public SystemCall(Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = 1;
        this.queue = new LinkedBlockingQueue<Query>();
        this.beingServedQueries = new PriorityQueue<Query>(1, new QueryComparator());// Tentativo ya que es solo de 1 espacio
    }

    public double entriesANewQuery(Query query) {
        if (numberOfFreeServers > 0) {
            numberOfFreeServers--;
            query.setArrivalTime(simulation.getTime());
            beingServedQueries.add(query);
            query.setDepartureTime(getGenerator().getNormal(1.5, 0.1) + query.getArrivalTime());
            return query.getDepartureTime();
        }
        return 0;
    }

    public void aQueryIsServed() {

    }

    public void rejectQuery(Query query) {

    }

    public Query aQueryFinished() {
        return null;
    }

    public boolean confirmAliveQuery(Query query) {
        return false;
    }
}
