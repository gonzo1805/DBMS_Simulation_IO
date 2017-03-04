package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.ModuleStatistics;

import java.util.concurrent.LinkedBlockingQueue;

import static ucr.group1.event.EventType.EXIT_PROCESSES_MANAGEMENT_MODULE;

/**
 * Created by Gonzalo and Daniel on 2/9/2017.
 */
public class ProcessesManagementModule extends Module<Query> {

    public Query beingServedQuery;

    public ProcessesManagementModule(Simulation simulation, Generator generator, Module nextModule) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = 1;
        this.numberOfServers = numberOfFreeServers;
        this.queue = new LinkedBlockingQueue<Query>();
        this.moduleStatistics = new ModuleStatistics(this, this.simulation);
        this.nextModule = nextModule;
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

    public void rejectQuery(Query query) {
        query.kill();
    }

    public Query aQueryFinished() {
        Query out = beingServedQuery;
        beingServedQuery = null;
        out.setBeingServed(false);
        out.addLifeSpan(simulation.getTime() - out.getArrivalTime());
        moduleStatistics.updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
        return out;
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
        moduleStatistics.updateTimeBetweenArrives(simulation.getTime());
        simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                " arrived to the processes management module");
        double exitTime = entriesANewQuery(actualEvent.getQuery());
        if (exitTime > -1) {
            simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                    " is now being processed");
            simulation.addEvent(new Event(EXIT_PROCESSES_MANAGEMENT_MODULE, exitTime, actualEvent.getQuery()));
        }
    }


    public void exitProcessesManagementModule(Event actualEvent){
        Query fromModule = aQueryFinished();
        if (!fromModule.getDead()) {
            simulation.addLineInTimeLog("The query " + fromModule.getId() + " is out from the processes " +
                    "management module");
            ((QueriesVerificationModule)nextModule).enterVerificationModuleEvent(actualEvent);
        }
        else{
            simulation.getQueryStatistics().aQueryIsKilled();
            simulation.releaseAConnectionServer();
        }
        if (!queue.isEmpty()) {
            Query toBeServed = queue.poll();
            toBeServed.setDepartureTime(simulation.getTime() + getGenerator().getNormal(1.5, 0.1));
            toBeServed.setBeingServed(true);
            beingServedQuery = toBeServed;
            simulation.addLineInTimeLog("The query " + toBeServed.getId() +
                    " is now being processed");
            Event nextEvent = new Event(EXIT_PROCESSES_MANAGEMENT_MODULE, toBeServed.getDepartureTime(), toBeServed);
            simulation.addEvent(nextEvent);
        } else {
            numberOfFreeServers++;
        }
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
