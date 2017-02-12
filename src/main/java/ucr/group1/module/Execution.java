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
    int totalServers;
    boolean aDddlIsWaiting;

    public Execution(int numberOfFreeServers, Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = numberOfFreeServers;
        this.totalServers = numberOfFreeServers;
        this.queue = new PriorityQueue<Query>(1000000 , new ExecutionComparator());
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers , new QueryComparator());
        this.aDddlIsWaiting = false;
    }

    public double entriesANewQuery(Query query) {
        if ((numberOfFreeServers > 0)&&(!aDddlIsWaiting)) {
            if(query.getPriority() > 1) {
                numberOfFreeServers--;
                query.setArrivalTime(simulation.getTime());
                beingServedQueries.add(query);
                query.setDepartureTime(((totalServers - numberOfFreeServers)*0.03) + query.getArrivalTime());
                return query.getDepartureTime();
            }
            else{
                aDddlIsWaiting = true;
                if(numberOfFreeServers == totalServers){
                    numberOfFreeServers--;
                    query.setArrivalTime(simulation.getTime());
                    beingServedQueries.add(query);
                    query.setDepartureTime(0.03 + query.getArrivalTime());
                    return query.getDepartureTime();
                }
                else{
                    ddlToBeExecuted = query;
                    return -1;
                }
            }
        }
        else{
            queue.add(query);
            return -1;
        }
    }

    public void rejectQuery(Query query) {
        query.kill();
    }

    public void aQueryIsServed(){}

    public Query aQueryFinished() {
        Query out = beingServedQueries.poll();
        if(out.getPriority() == 1){
            aDddlIsWaiting = false;
        }
        out.setStorageDuration(simulation.getTime() - out.getArrivalTime());
        numberOfFreeServers++;
        if(!aDddlIsWaiting){
            if(!queue.isEmpty()){
                Query query = queue.poll();
                if(query.getPriority() > 1){
                    numberOfFreeServers--;
                    beingServedQueries.add(query);
                    query.setDepartureTime(((totalServers - numberOfFreeServers)*0.03) + simulation.getTime());
                }
                else{
                    aDddlIsWaiting = true;
                    if(numberOfFreeServers == totalServers){
                        numberOfFreeServers--;
                        beingServedQueries.add(query);
                        query.setDepartureTime(0.03 + simulation.getTime());
                    }
                    else{
                        ddlToBeExecuted = query;
                    }
                }
            }
        }
        else if(numberOfFreeServers == totalServers){
            numberOfFreeServers--;
            beingServedQueries.add(ddlToBeExecuted);
            ddlToBeExecuted.setDepartureTime(0.03 + simulation.getTime());
        }
        return out;
    }

    public boolean confirmAliveQuery(Query query) {
        return false;
    }
}
