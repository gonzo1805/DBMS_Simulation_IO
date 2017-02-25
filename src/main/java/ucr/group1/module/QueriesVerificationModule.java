package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.simulation.Simulation;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.statistics.ModuleStatistics;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static ucr.group1.event.EventType.EXIT_VERIFICATION_MODULE;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class QueriesVerificationModule extends Module<Query> {

    public QueriesVerificationModule(int numberOfFreeServers, Simulation simulation, Generator generator, Module nextModule) {
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
        else {
            queue.add(query);
            return -1;
        }
    }

    public void rejectQuery(Query query) {
        query.getDead();
    }

    /**
     * @param query The query to calculate the service duration
     * @return A single service duration in the module
     */
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
        out.addLifeSpan(simulation.getTime() - out.getArrivalTime());
        moduleStatistics.updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
        return out;
    }

    public int getNumberOfQueriesOnQueue() {
        return queue.size();
    }

    public int getNumberOfQueriesBeingServed() {
        return beingServedQueries.size();
    }

    public void enterVerificationModuleEvent(Event actualEvent){
        moduleStatistics.updateTimeBetweenArrives(simulation.getTime());
        simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                " arrived to the queries verification module");
        double exitTime = entriesANewQuery(actualEvent.getQuery());
        if (exitTime > -1) {
            simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                    " is now being verified");
            simulation.addEvent(new Event(EXIT_VERIFICATION_MODULE, exitTime, actualEvent.getQuery()));
        }
    }

    public void exitVerificationModuleEvent(Event actualEvent){
        Query fromModule = aQueryFinished();// De que modulo viene
        if (!fromModule.getDead()) {
            simulation.addLineInTimeLog("The query " + fromModule.getId() + " is out from validation.");
            ((TransactionsModule)nextModule).enterTransactionsModuleEvent(actualEvent);
        } else {
            // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
            simulation.getQueryStatistics().aQueryIsKilled();
            simulation.releaseAConnectionServer();
        }
        if (!queue.isEmpty()) {
            Query toBeServed = queue.poll();
            toBeServed.setBeingServed(true);
            toBeServed.setDepartureTime(simulation.getTime() + getServiceDuration(toBeServed));
            beingServedQueries.add(toBeServed);
            simulation.addLineInTimeLog("The query " + toBeServed.getId() +
                    " is now attended in validation.");
            Event nextEvent = new Event(EXIT_VERIFICATION_MODULE, toBeServed.getDepartureTime(), toBeServed);
            simulation.addEvent(nextEvent);
        }
        else{
            numberOfFreeServers++;
        }
    }

    public void updateL_sStatistics() {
        moduleStatistics.updateL_S(beingServedQueries.size());
    }

    public void updateL_qStatistics() {
        moduleStatistics.updateL_Q(queue.size());
    }
}
