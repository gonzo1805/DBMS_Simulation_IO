package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.ModuleStatistics;

import java.util.PriorityQueue;

import static ucr.group1.event.EventType.EXIT_EXECUTION;
import static ucr.group1.event.EventType.RETURN_TO_CONNECTION;
import static ucr.group1.query.QueryType.type.DDL;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class QueriesExecutionModule extends Module<Query> {

    private Query ddlToBeExecuted;
    private boolean aDdlIsWaiting;
    private Query lastQueryObtainedFromQueue;
    private boolean entriesANewQueryFromQueue;

    public QueriesExecutionModule(int numberOfFreeServers, Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = numberOfFreeServers;
        this.numberOfServers = numberOfFreeServers;
        this.queue = new PriorityQueue<Query>(1000000 , new QueriesExecutionModuleComparator());
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers , new QueryComparator());
        this.aDdlIsWaiting = false;
        this.entriesANewQueryFromQueue = false;
        this.moduleStatistics = new ModuleStatistics(this,this.simulation);
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
        moduleStatistics.updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
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

    public boolean aQueryFromQueueIsNowBeingServed(){
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

    public void enterExecutionEvent(Event actualEvent){
        simulation.setTime(actualEvent.getTime());
        moduleStatistics.updateTimeBetweenArrives(actualEvent.getTime());
        simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                " arrived to execution.");
        double exitTime = entriesANewQuery(actualEvent.getQuery());
        if (exitTime > -1) {
            simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                    " is now attended in execution.");
            simulation.addEvent(new Event(EXIT_EXECUTION, exitTime, actualEvent.getQuery()));
        }
        simulation.finalizeEvent(actualEvent);
    }

    public void exitExecutionEvent(Event actualEvent){
        simulation.setTime(actualEvent.getTime());
        Query fromModule = aQueryFinished();// De que modulo viene
        if (!fromModule.getDead()) {
            simulation.addLineInTimeLog("The query " + fromModule.getId() + " is out from execution.");
            simulation.addEvent(new Event(RETURN_TO_CONNECTION, simulation.getTime(), fromModule));
        } else {
            // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
            simulation.getQueryStatistics().rejectAQuery();
            simulation.releaseAConnectionServer();
        }
        if (aQueryFromQueueIsNowBeingServed()) {
            Query nextQueryToExit = nextQueryFromQueueToBeOut();
            simulation.addLineInTimeLog("The query " + nextQueryToExit.getId() +
                    " is now attended in execution.");
            Event nextEvent = new Event(EXIT_EXECUTION, nextQueryToExit.getDepartureTime(), nextQueryToExit);
            actualEvent.getQuery().setNextEvent(nextEvent);
            simulation.addEvent(nextEvent);
        }
        simulation.finalizeEvent(actualEvent);
    }

    public void updateL_sStatistics(){
        moduleStatistics.updateL_S(beingServedQueries.size());
    }

    public void updateL_qStatistics(){
        moduleStatistics.updateL_Q(queue.size());
    }
}
