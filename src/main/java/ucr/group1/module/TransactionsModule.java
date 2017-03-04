package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.ModuleStatistics;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static ucr.group1.event.EventType.EXIT_TRANSACTIONS_MODULE;

/**
 * Created by Gonzalo and Daniel on 2/9/2017.
 */
public class TransactionsModule extends Module<Query> {

    public TransactionsModule(int numberOfFreeServers, Simulation simulation, Generator generator, Module nextModule) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = numberOfFreeServers;
        this.numberOfServers = numberOfFreeServers;
        this.queue = new LinkedBlockingQueue<Query>();
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers , new QueryComparator());
        this.moduleStatistics = new ModuleStatistics(this, this.simulation);
        this.nextModule = nextModule;
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

    }

    public void rejectQuery(Query query) {
        query.kill();
    }

    public Query aQueryFinished() {
        Query out = beingServedQueries.poll();
        out.addLifeSpan(simulation.getTime() - out.getArrivalTime());
        moduleStatistics.updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
        out.setBeingServed(false);
        return out;
    }

    /**
     * @param query The query to calculate the service duration
     * @return A single service duration in the module
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

    public int getNumberOfQueriesOnQueue() {
        return queue.size();
    }

    public int getNumberOfQueriesBeingServed() {
        return beingServedQueries.size();
    }


    public void enterTransactionsModuleEvent(Event actualEvent){
        moduleStatistics.updateTimeBetweenArrives(simulation.getTime());
        simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() + " arrived to the " +
                "transactions module");
        double exitTime = entriesANewQuery(actualEvent.getQuery());
        if (exitTime > -1) {
            simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                    " is now attended in the transactions module");
            simulation.addEvent(new Event(EXIT_TRANSACTIONS_MODULE, exitTime, actualEvent.getQuery()));
        }
    }

    public void exitTransactionsModuleEvent(Event actualEvent){
        Query fromModule = aQueryFinished();
        if (!fromModule.getDead()) {
            simulation.addLineInTimeLog("The query " + fromModule.getId() + " is out from the transactions module");
            ((QueriesExecutionModule)nextModule).enterExecutionModuleEvent(actualEvent);
        } else {
            // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
            simulation.getQueryStatistics().aQueryIsKilled();
            simulation.releaseAConnectionServer();
        }
        if(!queue.isEmpty()){
            Query toBeServed = queue.poll();
            toBeServed.setBeingServed(true);
            toBeServed.setDepartureTime(simulation.getTime() + getServiceDuration(toBeServed));
            beingServedQueries.add(toBeServed);
            simulation.addLineInTimeLog("The query " + toBeServed.getId() +
                    " is now attended in the transactions module");
            Event nextEvent = new Event(EXIT_TRANSACTIONS_MODULE, toBeServed.getDepartureTime(), toBeServed);
            simulation.addEvent(nextEvent);
        }
        else{
            numberOfFreeServers++;
        }
        simulation.finalizeEvent(actualEvent);
    }

    public void updateL_sStatistics() {
        moduleStatistics.updateL_S(beingServedQueries.size());
    }

    public void updateL_qStatistics() {
        moduleStatistics.updateL_Q(queue.size());
    }
}
