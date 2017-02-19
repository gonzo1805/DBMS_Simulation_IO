package ucr.group1.module;

import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Storage extends Module<Query> {

    private Query lastQueryObtainedFromQueue;
    private boolean entriesANewQueryFromQueue;

    public Storage(int numberOfFreeServers, Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = numberOfFreeServers;
        this.numberOfServers = numberOfFreeServers;
        this.queue = new LinkedBlockingQueue<Query>();
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers , new QueryComparator());
        this.entriesANewQueryFromQueue = false;
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
        else{
            queue.add(query);
            return -1;
        }
    }

    public void aQueryIsServed() {
        Query toBeServed = queue.poll();
        lastQueryObtainedFromQueue = toBeServed;
        toBeServed.setBeingServed(true);
        toBeServed.setDepartureTime(simulation.getTime() + getServiceDuration(toBeServed));
        beingServedQueries.add(toBeServed);
    }

    public void rejectQuery(Query query) {
        query.kill();
    }

    public Query aQueryFinished() {
        Query out = beingServedQueries.poll();
        out.addLifeSpan(simulation.getTime() - out.getArrivalTime());
        simulation.getStorageStatistics().updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
        out.setBeingServed(false);
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

    public double getServiceDuration(Query query) {
        double duration = 0;
        int nBlocks = 0;
        switch(query.getType()){
            case DDL:
                // UPDATE DATABASE SCHEME
                duration += 0.5;
                break;
            case UPDATE:
                // UPDATE DATABASE SCHEME
                duration += 1;
                break;
            case JOIN:
                nBlocks += (int)getGenerator().getRandomUniform(1,17);
                nBlocks += (int)getGenerator().getRandomUniform(1,13);
                query.setChargedBlocks(nBlocks);
                // LOAD BLOCKS INTO DATA BASE
                duration += ((double)nBlocks / 10);
                // UPDATE DATABASE SCHEME
                duration += (double)(nBlocks^2) / 1000;
                break;
            case SELECT:
                nBlocks += (int)getGenerator().getRandomUniform(1,65);
                query.setChargedBlocks(nBlocks);
                // LOAD BLOCKS INTO DATA BASE
                duration += (double)nBlocks / 10;
                // UPDATE DATABASE SCHEME
                duration += (double)(nBlocks^2) / 1000;
                break;
        }
        return duration;
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
        return beingServedQueries.size();
    }
}
