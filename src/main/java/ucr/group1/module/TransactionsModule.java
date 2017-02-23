package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.ModuleStatistics;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static ucr.group1.event.EventType.ENTER_EXECUTION_MODULE;
import static ucr.group1.event.EventType.EXIT_TRANSACTIONS_MODULE;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class TransactionsModule extends Module<Query> {

    private Query lastQueryObtainedFromQueue;
    private boolean entriesANewQueryFromQueue;

    public TransactionsModule(int numberOfFreeServers, Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = numberOfFreeServers;
        this.numberOfServers = numberOfFreeServers;
        this.queue = new LinkedBlockingQueue<Query>();
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers , new QueryComparator());
        this.entriesANewQueryFromQueue = false;
        this.moduleStatistics = new ModuleStatistics(this, this.simulation);
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
        moduleStatistics.updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
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

    /**
     *
     * @param query The query to calculate the service duration
     * @return      A single service duration in the module
     */
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

    public void enterTransactionsModuleEvent(Event actualEvent){
        simulation.setTime(actualEvent.getTime());
        moduleStatistics.updateTimeBetweenArrives(simulation.getTime());
        simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() + " arrived to storage.");
        double exitTime = entriesANewQuery(actualEvent.getQuery());
        if (exitTime > -1) {
            simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                    " is now attended in storage.");
            simulation.addEvent(new Event(EXIT_TRANSACTIONS_MODULE, exitTime, actualEvent.getQuery()));
        }
        simulation.finalizeEvent(actualEvent);
    }

    public void exitTransactionsModuleEvent(Event actualEvent){
        simulation.setTime(actualEvent.getTime());
        Query fromModule = aQueryFinished();// De que modulo viene
        if (!fromModule.getDead()) {
            simulation.addLineInTimeLog("The query " + fromModule.getId() + " is out from storage.");
            simulation.addEvent(new Event(ENTER_EXECUTION_MODULE, simulation.getTime(), fromModule));
        } else {
            // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
            simulation.getQueryStatistics().rejectAQuery();
            simulation.releaseAConnectionServer();
        }
        if (aQueryFromQueueIsNowBeingServed()) {
            Query nextQueryToExit = nextQueryFromQueueToBeOut();
            simulation.addLineInTimeLog("The query " + nextQueryToExit.getId() +
                    " is now attended in storage.");
            Event nextEvent = new Event(EXIT_TRANSACTIONS_MODULE, nextQueryToExit.getDepartureTime(), nextQueryToExit);
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
