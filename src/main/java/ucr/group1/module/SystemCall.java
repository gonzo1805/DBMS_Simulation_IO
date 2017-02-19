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

    public Query beingServedQuery;
    private Query lastQueryObtainedFromQueue;
    private boolean entriesANewQueryFromQueue;

    public SystemCall(Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = 1;
        this.numberOfServers = numberOfFreeServers;
        this.queue = new LinkedBlockingQueue<Query>();
        this.entriesANewQueryFromQueue = false;
    }

    public double entriesANewQuery(Query query) {
        query.setArrivalTime(simulation.getTime());
        if (numberOfFreeServers > 0) {
            numberOfFreeServers--;
            beingServedQuery = query;
            query.setDepartureTime(getGenerator().getNormal(1.5, 0.1) + query.getArrivalTime());
            query.setBeingServed(true);
            return query.getDepartureTime();
        }
        else{
            queue.add(query);
            return -1;
        }
    }

    public void aQueryIsServed() {
        Query toBeServed = queue.poll();
        lastQueryObtainedFromQueue = toBeServed;
        toBeServed.setDepartureTime(simulation.getTime() + getGenerator().getNormal(1.5, 0.1));
        toBeServed.setBeingServed(true);
        beingServedQuery = toBeServed;
    }

    public void rejectQuery(Query query) {
        query.kill();
    }

    public Query aQueryFinished(){
        Query out = beingServedQuery;
        beingServedQuery = null;
        out.setBeingServed(false);
        out.addLifeSpan(simulation.getTime() - out.getArrivalTime());
        simulation.getSystemCallStatistics().updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
        if(!queue.isEmpty()){
            aQueryIsServed();
            entriesANewQueryFromQueue = true;
        }
        else{
            numberOfFreeServers++;
            entriesANewQueryFromQueue = false;
        }
        return out;
    }

    public boolean confirmAliveQuery(Query query) {
        return !query.getDead();
    }

    public boolean isAQueryBeingServed(){
        return entriesANewQueryFromQueue;
    }

    public Query nextQueryFromQueueToBeOut(){
        return lastQueryObtainedFromQueue;
    }

    public int getNumberOfQueriesOnQueue(){
        return queue.size();
    }

    public int getNumberOfQueriesBeingServed(){
        if(beingServedQuery == null){
            return 0;
        }
        return 1;
    }
}
