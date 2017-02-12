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
        query.setArrivalTime(simulation.getTime());
        if (numberOfFreeServers > 0) {
            numberOfFreeServers--;
            beingServedQueries.add(query);
            query.setDepartureTime(getGenerator().getNormal(1.5, 0.1) + query.getArrivalTime());
            return query.getDepartureTime();
        }
        queue.add(query);
        return -1;
    }

    public void aQueryIsServed() {
        Query toBeServed = queue.poll();
        toBeServed.setDepartureTime(simulation.getTime() + getGenerator().getNormal(1.5, 0.1));
        beingServedQueries.add(toBeServed);
    }

    public void rejectQuery(Query query) {
        query.kill();
    }

    public Query aQueryFinished(){
        Query out = beingServedQueries.poll();
        out.setSystemCallDuration(simulation.getTime() - out.getArrivalTime());
        if(!queue.isEmpty()){
            aQueryIsServed();
        }
        else{
            numberOfFreeServers++;
        }
        return out;
    }

    public boolean confirmAliveQuery(Query query) {
        return !query.getDead();
    }
}
