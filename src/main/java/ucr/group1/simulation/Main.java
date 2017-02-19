package ucr.group1.simulation;

import ucr.group1.event.Event;
import ucr.group1.module.Connection;
import ucr.group1.query.Query;
import ucr.group1.query.QueryType;

import java.util.Queue;

import static ucr.group1.event.Event.eventType.*;

/**
 * Created by Daniel on 12/2/2017.
 */
public class Main {
    public static void main(String[]  args){
        int timeOut = 15;
        Simulation simulation = new Simulation(20,8,10,2,
                timeOut,false, 0);
        int idAsigner = 1;
        Event firstEvent = new Event(ENTER_CONNECTION,0, new Query(idAsigner++, simulation.getGenerator()));
        simulation.addEvent(firstEvent);
        while(idAsigner < 100/* REVISAR CONDICIÓN DESPUÉS*/){
            Event actualEvent = simulation.getNextEvent();
            double exitTime;
            Query fromModule;
            switch(actualEvent.getEventType()){
                case ENTER_CONNECTION:
                    simulation.setTime(actualEvent.getTime());
                    simulation.getConnectionStatistics().updateTimeBetweenArrives(simulation.getTime());
                    System.out.println(simulation.getTimeInHHMMSS()+"A new query is trying to get a connection");
                    exitTime = simulation.getConnection().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        System.out.println(simulation.getTimeInHHMMSS() + "The query " + actualEvent.getQuery().getId() +
                                " the query is connected with the database");
                        Event nextEvent = new Event(ENTER_SYSTEMCALL, exitTime, actualEvent.getQuery());
                        actualEvent.getQuery().setNextEvent(nextEvent);
                        simulation.addEvent(nextEvent);
                        Event killEvent = new Event(KILL, timeOut + actualEvent.getTime(), actualEvent.getQuery());
                        actualEvent.getQuery().setKillEvent(killEvent);
                        simulation.addEvent(killEvent);
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                        System.out.println(simulation.getTimeInHHMMSS() + "The system reached the maximum of simultaneous " +
                                "connections, the new query is rejected");
                    }
                    simulation.addEvent(new Event(ENTER_CONNECTION, simulation.getTime() + simulation.getGenerator().getExponential(1.7142),
                            new Query(idAsigner++, simulation.getGenerator())));
                    simulation.finalizeEvent(actualEvent);
                    break;
                case RETURN_TO_CONNECTION:
                    simulation.setTime(actualEvent.getTime());
                    ((Connection) simulation.getConnection()).aQueryHasReturned(actualEvent.getQuery());
                    simulation.addEvent(new Event(EXIT_CONNECTION, actualEvent.getQuery().getDepartureTime(), actualEvent.getQuery()));
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_CONNECTION:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getConnection().aQueryFinished();// De que modulo viene
                    System.out.println(simulation.getTimeInHHMMSS()+"The query " + fromModule.getId() + " is out from connection.");
                    simulation.thisQueryKillNeverHappened(fromModule);
                    simulation.finalizeEvent(actualEvent);
                    break;
                case ENTER_SYSTEMCALL:
                    simulation.setTime(actualEvent.getTime());
                    simulation.getSystemCallStatistics().updateTimeBetweenArrives(simulation.getTime());
                    System.out.println(simulation.getTimeInHHMMSS() + "The query " + actualEvent.getQuery().getId() +
                            " arrived to systemcall.");
                    exitTime = simulation.getSystemCall().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        System.out.println(simulation.getTimeInHHMMSS() + "The query " + actualEvent.getQuery().getId() +
                                " is now attended in systemcall.");
                        simulation.addEvent(new Event(EXIT_SYSTEMCALL, exitTime, actualEvent.getQuery()));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_SYSTEMCALL:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getSystemCall().aQueryFinished();// De que modulo viene
                    if(!fromModule.getDead()){
                        System.out.println(simulation.getTimeInHHMMSS()+"The query " + fromModule.getId() + " is out from systemcall.");
                        simulation.addEvent(new Event(ENTER_VALIDATION, simulation.getTime(), fromModule));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    if(simulation.getSystemCall().isAQueryBeingServed()){
                        Query nextQueryToExit = simulation.getSystemCall().nextQueryFromQueueToBeOut();
                        System.out.println(simulation.getTimeInHHMMSS() + "The query " + nextQueryToExit.getId() +
                                " is now attended in systemcall.");
                        Event nextEvent = new Event(EXIT_SYSTEMCALL, nextQueryToExit.getDepartureTime(), nextQueryToExit);
                        actualEvent.getQuery().setNextEvent(nextEvent);
                        simulation.addEvent(nextEvent);
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case ENTER_VALIDATION:
                    simulation.setTime(actualEvent.getTime());
                    simulation.getValidationStatistics().updateTimeBetweenArrives(simulation.getTime());
                    System.out.println(simulation.getTimeInHHMMSS() + "The query " + actualEvent.getQuery().getId() +
                            " arrived to validation.");
                    exitTime = simulation.getValidation().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        System.out.println(simulation.getTimeInHHMMSS() + "The query " + actualEvent.getQuery().getId() +
                                " is now attended in validation.");
                        simulation.addEvent(new Event(EXIT_VALIDATION, exitTime, actualEvent.getQuery()));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_VALIDATION:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getValidation().aQueryFinished();// De que modulo viene
                    if(!fromModule.getDead()){
                        System.out.println(simulation.getTimeInHHMMSS()+"The query " + fromModule.getId() + " is out from validation.");
                        simulation.addEvent(new Event(ENTER_STORAGE, simulation.getTime(), fromModule));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    if(simulation.getValidation().isAQueryBeingServed()){
                        Query nextQueryToExit = simulation.getValidation().nextQueryFromQueueToBeOut();
                        System.out.println(simulation.getTimeInHHMMSS() + "The query " + nextQueryToExit.getId() +
                                " is now attended in validation.");
                        Event nextEvent = new Event(EXIT_VALIDATION, nextQueryToExit.getDepartureTime(), nextQueryToExit);
                        actualEvent.getQuery().setNextEvent(nextEvent);
                        simulation.addEvent(nextEvent);
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case ENTER_STORAGE:
                    simulation.setTime(actualEvent.getTime());
                    simulation.getStorageStatistics().updateTimeBetweenArrives(simulation.getTime());
                    System.out.println(simulation.getTimeInHHMMSS()+"The query " + actualEvent.getQuery().getId() + " arrived to storage.");
                    exitTime = simulation.getStorage().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        System.out.println(simulation.getTimeInHHMMSS() + "The query " + actualEvent.getQuery().getId() +
                                " is now attended in storage.");
                        simulation.addEvent(new Event(EXIT_STORAGE, exitTime, actualEvent.getQuery()));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_STORAGE:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getStorage().aQueryFinished();// De que modulo viene
                    if(!fromModule.getDead()){
                        System.out.println(simulation.getTimeInHHMMSS()+"The query " + fromModule.getId() + " is out from storage.");
                        simulation.addEvent(new Event(ENTER_EXECUTION, simulation.getTime(), fromModule));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    if(simulation.getStorage().isAQueryBeingServed()){
                        Query nextQueryToExit = simulation.getStorage().nextQueryFromQueueToBeOut();
                        System.out.println(simulation.getTimeInHHMMSS() + "The query " + nextQueryToExit.getId() +
                                " is now attended in storage.");
                        Event nextEvent = new Event(EXIT_STORAGE, nextQueryToExit.getDepartureTime(), nextQueryToExit);
                        actualEvent.getQuery().setNextEvent(nextEvent);
                        simulation.addEvent(nextEvent);
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case ENTER_EXECUTION:
                    simulation.setTime(actualEvent.getTime());
                    simulation.getExecutionStatistics().updateTimeBetweenArrives(actualEvent.getTime());
                    System.out.println(simulation.getTimeInHHMMSS() + "The query " + actualEvent.getQuery().getId() +
                            " arrived to execution.");
                    exitTime = simulation.getExecution().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        System.out.println(simulation.getTimeInHHMMSS() + "The query " + actualEvent.getQuery().getId() +
                                " is now attended in execution.");
                        simulation.addEvent(new Event(EXIT_EXECUTION, exitTime, actualEvent.getQuery()));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_EXECUTION:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getExecution().aQueryFinished();// De que modulo viene
                    if(!fromModule.getDead()){
                        System.out.println(simulation.getTimeInHHMMSS()+"The query " + fromModule.getId() + " is out from execution.");
                        simulation.addEvent(new Event(RETURN_TO_CONNECTION, simulation.getTime(), fromModule));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    if(simulation.getExecution().isAQueryBeingServed()){
                        Query nextQueryToExit = simulation.getExecution().nextQueryFromQueueToBeOut();
                        System.out.println(simulation.getTimeInHHMMSS() + "The query " + nextQueryToExit.getId() +
                                " is now attended in execution.");
                        Event nextEvent = new Event(EXIT_EXECUTION, nextQueryToExit.getDepartureTime(), nextQueryToExit);
                        actualEvent.getQuery().setNextEvent(nextEvent);
                        simulation.addEvent(nextEvent);
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case KILL:
                    if(actualEvent.getQuery().isBeingServed()){
                        actualEvent.getQuery().kill();
                    }
                    else{
                        Queue queue = simulation.getDeadQueryQueue(actualEvent.getQuery());
                        if(queue != null){
                            queue.remove(actualEvent.getQuery());
                        }
                        simulation.thisQueryWereKilledBeforeReachTheNextEvent(actualEvent.getQuery());
                    }
                    break;
            }
            simulation.getConnectionStatistics().updateL_S(simulation.getConnection().getNumberOfQueriesBeingServed());
            simulation.getSystemCallStatistics().updateL_Q(simulation.getSystemCall().getNumberOfQueriesOnQueue());
            simulation.getSystemCallStatistics().updateL_S(simulation.getSystemCall().getNumberOfQueriesBeingServed());
            simulation.getValidationStatistics().updateL_Q(simulation.getValidation().getNumberOfQueriesOnQueue());
            simulation.getValidationStatistics().updateL_S(simulation.getValidation().getNumberOfQueriesBeingServed());
            simulation.getStorageStatistics().updateL_Q(simulation.getStorage().getNumberOfQueriesOnQueue());
            simulation.getStorageStatistics().updateL_S(simulation.getStorage().getNumberOfQueriesBeingServed());
            simulation.getExecutionStatistics().updateL_Q(simulation.getExecution().getNumberOfQueriesOnQueue());
            simulation.getExecutionStatistics().updateL_S(simulation.getExecution().getNumberOfQueriesBeingServed());
        }
        /*QueryType queryType = new QueryType(simulation.getGenerator());
        System.out.println(simulation.getValidationStatistics().getAverageTime(queryType));
        System.out.println(simulation.getValidationStatistics().getL());
        System.out.println(simulation.getValidationStatistics().getL_q());
        System.out.println(simulation.getValidationStatistics().getL_s());
        System.out.println(simulation.getValidationStatistics().getLambda());
        System.out.println(simulation.getValidationStatistics().getLeisureTime());*/
    }
}
