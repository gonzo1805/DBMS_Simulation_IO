package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.generator.Generator;
import ucr.group1.query.Query;
import ucr.group1.query.QueryType;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.ModuleStatistics;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static ucr.group1.event.EventType.EXIT_EXECUTION_MODULE;
import static ucr.group1.query.QueryLabel.DDL;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class QueriesExecutionModule extends Module<Query> {

    private Query ddlToBeExecuted;
    private boolean aDdlIsWaiting;

    public QueriesExecutionModule(int numberOfFreeServers, Simulation simulation, Generator generator, Module nextModule) {
        this.generator = generator;
        this.simulation = simulation;
        this.numberOfFreeServers = numberOfFreeServers;
        this.numberOfServers = numberOfFreeServers;
        this.queue = new PriorityQueue<Query>(1000000, new QueriesExecutionModuleComparator());
        this.beingServedQueries = new PriorityQueue<Query>(numberOfFreeServers , new QueryComparator());
        this.aDdlIsWaiting = false;
        this.moduleStatistics = new ModuleStatistics(this, this.simulation);
        this.nextModule = nextModule;
    }

    public double entriesANewQuery(Query query) {
        if ((numberOfFreeServers > 0)&&(!aDdlIsWaiting)) {
            if(query.getPriority() > 1) {
                numberOfFreeServers--;
                query.setArrivalTime(simulation.getTime());
                beingServedQueries.add(query);
                query.setDepartureTime(((numberOfServers - numberOfFreeServers) * 0.03) + query.getArrivalTime());
                query.setBeingServed(true);
                return query.getDepartureTime();
            }
            else{
                aDdlIsWaiting = true;
                if (numberOfFreeServers == numberOfServers) {
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

    public Query aQueryFinished() {
        Query out = beingServedQueries.poll();
        out.setBeingServed(false);
        if(out.getType() == DDL){
            aDdlIsWaiting = false;
        }
        out.addLifeSpan(simulation.getTime() - out.getArrivalTime());
        moduleStatistics.updateModuleTime(out, simulation.getTime() - out.getArrivalTime());
        numberOfFreeServers++;
        return out;
    }

    public int getNumberOfQueriesOnQueue() {
        return queue.size();
    }

    public int getNumberOfQueriesBeingServed() {
        return beingServedQueries.size();
    }

    public void enterExecutionModuleEvent(Event actualEvent){
        moduleStatistics.updateTimeBetweenArrives(actualEvent.getTime());
        simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                " arrived to the queries execution module");
        double exitTime = entriesANewQuery(actualEvent.getQuery());
        if (exitTime > -1) {
            simulation.addLineInTimeLog("The query " + actualEvent.getQuery().getId() +
                    " is now being executed");
            simulation.addEvent(new Event(EXIT_EXECUTION_MODULE, exitTime, actualEvent.getQuery()));
        }
    }

    public void exitExecutionModuleEvent(Event actualEvent){
        Query fromModule = aQueryFinished();
        if (!fromModule.getDead()) {
            simulation.addLineInTimeLog("The query " + fromModule.getId() + " is out from the queries execution " +
            "module");
            ((ClientManagementModule)nextModule).returnToClientManagementModuleEvent(actualEvent);
        } else {
            // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
            simulation.getQueryStatistics().aQueryIsKilled();
            simulation.releaseAConnectionServer();
        }
        if(!aDdlIsWaiting){
            if(!queue.isEmpty()){
                Query query = queue.poll();
                if(query.getType() != DDL){
                    numberOfFreeServers--;
                    beingServedQueries.add(query);
                    query.setBeingServed(true);
                    query.setDepartureTime(((numberOfServers - numberOfFreeServers) * 0.03) + simulation.getTime());
                    aQueryIsNowBeingServed(query);
                }
                else{
                    aDdlIsWaiting = true;
                    if (numberOfFreeServers == numberOfServers) {
                        numberOfFreeServers--;
                        beingServedQueries.add(query);
                        query.setBeingServed(true);
                        query.setDepartureTime(0.03 + simulation.getTime());
                        aQueryIsNowBeingServed(query);
                    }
                    else{
                        ddlToBeExecuted = query;
                    }
                }
            }
            else{
            }
        } else if (numberOfFreeServers == numberOfServers) {
            numberOfFreeServers--;
            beingServedQueries.add(ddlToBeExecuted);
            ddlToBeExecuted.setDepartureTime(0.03 + simulation.getTime());
            aQueryIsNowBeingServed(ddlToBeExecuted);
        }
    }

    public void aQueryIsNowBeingServed(Query toBeServed){
        simulation.addLineInTimeLog("The query " + toBeServed.getId() +
                " is now being executed");
        Event nextEvent = new Event(EXIT_EXECUTION_MODULE, toBeServed.getDepartureTime(), toBeServed);
        simulation.addEvent(nextEvent);
    }

    public void updateL_sStatistics() {
        moduleStatistics.updateL_S(beingServedQueries.size());
    }

    public void updateL_qStatistics() {
        moduleStatistics.updateL_Q(queue.size());
    }
}
