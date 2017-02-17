package ucr.group1.module;
import ucr.group1.simulation.Simulation;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Validation extends Module<Query> {

    public Validation(int numberOfFreeServers, Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = numberOfFreeServers;
        this.queue = new LinkedBlockingQueue<Query>();
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers , new QueryComparator());
    }

    public double entriesANewQuery(Query query) {
        query.setArrivalTime(simulation.getTime());
        if (numberOfFreeServers > 0) {
            numberOfFreeServers--;
            beingServedQueries.add(query);
            query.setDepartureTime(query.getArrivalTime() + getServiceDuration(query));
            query.setBeingServed(true);
            return query.getDepartureTime();
        }
        queue.add(query);
        return -1;
    }

    public void aQueryIsServed() {
        Query toBeServed = queue.poll();
        toBeServed.setBeingServed(true);
        toBeServed.setDepartureTime(simulation.getTime() + getServiceDuration(toBeServed));
        beingServedQueries.add(toBeServed);
    }

    public void rejectQuery(Query query) {
        query.getDead();
    }

    public double getServiceDuration(Query query) {
        double duration = 0;
        // LEXIC VALIDATION
        double randomProb = generator.getProbability();
        if(randomProb < 0.7){
            duration += 0.1;
        }
        else{
            duration += 0.4;
        }
        // SINTACTIC VALIDATION
        duration += generator.getRandomUniform(0,0.8);
        // SEMANTIC VALIDATION
        duration += generator.getNormal(1,0.5);
        // PERMIT VERIFICATION
        duration += generator.getExponential(0.7);
        // QUERY OPTIMIZATION
        if(query.getReadOnly()){
            duration += 0.1;
        }
        else{
            duration += 0.5;
        }
        return duration;
    }

    public Query aQueryFinished() {
        Query out = beingServedQueries.poll();
        out.setBeingServed(true);
        out.setValidationDuration(simulation.getTime() - out.getArrivalTime());
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

    public boolean isAQueryBeingServed(){
        return !beingServedQueries.isEmpty();
    }

    public Query nextQueryToBeOut(){
        return beingServedQueries.peek();
    }
}
