package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.ModuleStatistics;

import java.util.concurrent.LinkedBlockingQueue;

import static ucr.group1.event.EventType.ENTER_VERIFICATION_MODULE;
import static ucr.group1.event.EventType.EXIT_PROCESSES_MANAGEMENT_MODULE;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class ProcessesManagementModule extends Module<Query> {

    public Query beingServedQuery;
    private Query lastQueryObtainedFromQueue;
    private boolean entriesANewQueryFromQueue;

    public ProcessesManagementModule(Simulation simulation, Generator generator) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = 1;
        this.numberOfServers = numberOfFreeServers;
        this.queue = new LinkedBlockingQueue<Query>();
        this.entriesANewQueryFromQueue = false;
        this.moduleStatistics = new ModuleStatistics(this, this.simulation);
    }

    public double entriesANewQuery(Query query) {
        query.setArrivalTime(simulation.getTime());
        if (numberOfFreeServers > 0) {
            numberOfFreeServers--;
            beingServedQuery = query;
            query.setDepartureTime(getGenerator().getNormal(1.5, 0.1) + query.getArrivalTime());
            query.setBeingServed(true);
            return query.getDepartureTime();
        } else {
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

    public Query aQueryFinished() {
        Query out = beingServedQuery;
        beingServedQuery = null;
        out.setBeingServed(false);
        out.addLifeSpan(simulation.getTime() - out.getArrivalTime());
        moduleStatistics.updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
        if (!queue.isEmpty()) {
            aQueryIsServed();
            entriesANewQueryFromQueue = true;
        } else {
            numberOfFreeServers++;
            entriesANewQueryFromQueue = false;
        }
        return out;
    }

    public boolean aQueryFromQueueIsNowBeingServed() {
        return entriesANewQueryFromQueue;
    }

    public Query nextQueryFromQueueToBeOut() {
        return lastQueryObtainedFromQueue;
    }

    public int getNumberOfQueriesOnQueue() {
        return queue.size();
    }

    public int getNumberOfQueriesBeingServed() {
        if (beingServedQuery == null) {
            return 0;
        }
        return 1;
    }


    public void enterProcessesManagementModule(Event actualEvent){

        simulation.setTime(actualEvent.getTime());
        moduleStatistics.updateTimeBetweenArrives(simulation.getTime());
        simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                " arrived to systemcall.");
        double exitTime = entriesANewQuery(actualEvent.getQuery());
        if (exitTime > -1) {
            simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                    " is now attended in systemcall.");
            simulation.addEvent(new Event(EXIT_PROCESSES_MANAGEMENT_MODULE, exitTime, actualEvent.getQuery()));
        }
        simulation.finalizeEvent(actualEvent);
    }


    public void exitProcessesManagementModule(Event actualEvent){

        simulation.setTime(actualEvent.getTime());
        Query fromModule = aQueryFinished();// De que modulo viene
        if (!fromModule.getDead()) {
            simulation.addLineInTimeLog("The query " + fromModule.getId() + " is out from systemcall.");

            simulation.addEvent(new Event(ENTER_VERIFICATION_MODULE, simulation.getTime(), fromModule));
        }
        else{

            // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
            simulation.getQueryStatistics().rejectAQuery();
            simulation.releaseAConnectionServer();
        }
        if (aQueryFromQueueIsNowBeingServed()) {
            Query nextQueryToExit = nextQueryFromQueueToBeOut();
            simulation.addLineInTimeLog("The query " + nextQueryToExit.getId() +
                    " is now attended in systemcall.");
            Event nextEvent = new Event(EXIT_PROCESSES_MANAGEMENT_MODULE, nextQueryToExit.getDepartureTime(), nextQueryToExit);
            actualEvent.getQuery().setNextEvent(nextEvent);
            simulation.addEvent(nextEvent);
        }
        simulation.finalizeEvent(actualEvent);
    }

    public void updateL_sStatistics() {
        if (beingServedQuery != null) {
            moduleStatistics.updateL_S(1);
        } else {
            moduleStatistics.updateL_S(0);
        }
    }

    public void updateL_qStatistics() {
        moduleStatistics.updateL_Q(queue.size());
    }
}
