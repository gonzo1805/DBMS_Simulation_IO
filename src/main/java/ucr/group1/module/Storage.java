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

    public Storage(int numberOfFreeServers, Simulation simulation, Generator generator) {
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
            return query.getDepartureTime();
        }
        queue.add(query);
        return -1;
    }

    public void aQueryIsServed() {
        Query toBeServed = queue.poll();
        toBeServed.setDepartureTime(simulation.getTime() + getServiceDuration(toBeServed));
        beingServedQueries.add(toBeServed);
    }

    public void rejectQuery(Query query) {
        query.kill();
    }

    public Query aQueryFinished() {
        Query out = beingServedQueries.poll();
        out.setStorageDuration(simulation.getTime() - out.getArrivalTime());
        if(!queue.isEmpty()){
            aQueryIsServed();
        }
        else{
            numberOfFreeServers++;
        }
        return out;
    }

    public double getServiceDuration(Query query) {
        double duration = 0;
        int nBlocks = 0;
        switch(query.getPriority()){
            case 1:
                // UPDATE DATABASE SCHEME
                duration += 0.5;
                break;
            case 2:
                // UPDATE DATABASE SCHEME
                duration += 1;
                break;
            case 3:
                nBlocks += (int)getGenerator().getRandomUniform(1,17);
                nBlocks += (int)getGenerator().getRandomUniform(1,13);
                // LOAD BLOCKS INTO DATA BASE
                duration += ((double)nBlocks / 10);
                // UPDATE DATABASE SCHEME
                duration += (double)(nBlocks^2) / 1000;
                // Return result to conection module
                duration += (double)nBlocks / 6;
                break;
            case 4:
                nBlocks += (int)getGenerator().getRandomUniform(1,65);
                // LOAD BLOCKS INTO DATA BASE
                duration += (double)nBlocks / 10;
                // UPDATE DATABASE SCHEME
                duration += (double)(nBlocks^2) / 1000;
                // Return result to conection module
                duration += (double)nBlocks / 6;
                break;
        }
        return duration;
    }

    public boolean confirmAliveQuery(Query query) {
        return !query.getDead();
    }
}
