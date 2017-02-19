package ucr.group1.module;

import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.query.QueryType;
import ucr.group1.simulation.Simulation;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static ucr.group1.query.QueryType.type.DDL;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Execution extends Module<Query> {

    private Query ddlToBeExecuted;
    private boolean aDdlIsWaiting;
    private Query lastQueryObtainedFromQueue;
    private boolean entriesANewQueryFromQueue;

    public Execution(int numberOfFreeServers, Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = numberOfFreeServers;
        this.numberOfServers = numberOfFreeServers;
        this.queue = new PriorityQueue<Query>(1000000 , new ExecutionComparator());
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers , new QueryComparator());
        this.aDdlIsWaiting = false;
        this.entriesANewQueryFromQueue = false;
    }

    public double entriesANewQuery(Query query) {
        if ((numberOfFreeServers > 0)&&(!aDdlIsWaiting)) {
            if(query.getPriority() > 1) {
                numberOfFreeServers--;
                query.setArrivalTime(simulation.getTime());
                beingServedQueries.add(query);
                query.setDepartureTime(((numberOfServers - numberOfFreeServers)*0.03) + query.getArrivalTime());
                query.setBeingServed(true);
                return query.getDepartureTime();
            }
            else{
                aDdlIsWaiting = true;
                if(numberOfFreeServers == numberOfServers){
                    numberOfFreeServers--;
                    query.setArrivalTime(simulation.getTime());
                    beingServedQueries.add(query);
                    query.setDepartureTime(0.03 + query.getArrivalTime());
                    query.setBeingServed(true);
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
        out.setBeingServed(false);
        if(out.getType() == DDL){
            aDdlIsWaiting = false;
        }
        out.addLifeSpan(simulation.getTime() - out.getArrivalTime());
        simulation.getExecutionStatistics().updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
        numberOfFreeServers++;
        if(!aDdlIsWaiting){
            if(!queue.isEmpty()){
                Query query = queue.poll();
                if(query.getType() != DDL){
                    numberOfFreeServers--;
                    beingServedQueries.add(query);
                    lastQueryObtainedFromQueue = query;
                    query.setBeingServed(true);
                    query.setDepartureTime(((numberOfServers - numberOfFreeServers)*0.03) + simulation.getTime());
                    entriesANewQueryFromQueue = true;
                }
                else{
                    aDdlIsWaiting = true;
                    if(numberOfFreeServers == numberOfServers){
                        numberOfFreeServers--;
                        lastQueryObtainedFromQueue = query;
                        beingServedQueries.add(query);
                        query.setBeingServed(true);
                        query.setDepartureTime(0.03 + simulation.getTime());
                        entriesANewQueryFromQueue = true;
                    }
                    else{
                        ddlToBeExecuted = query;
                        entriesANewQueryFromQueue = false;
                    }
                }
            }
            else{
                entriesANewQueryFromQueue = false;
            }
        }
        else if(numberOfFreeServers == numberOfServers){
            numberOfFreeServers--;
            lastQueryObtainedFromQueue = ddlToBeExecuted;
            beingServedQueries.add(ddlToBeExecuted);
            ddlToBeExecuted.setDepartureTime(0.03 + simulation.getTime());
            entriesANewQueryFromQueue = true;
        }
        else{
            entriesANewQueryFromQueue = false;
        }
        return out;
    }

    public boolean confirmAliveQuery(Query query) {
        return false;
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
