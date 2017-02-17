package ucr.group1.simulation;

import ucr.group1.event.Event;
import ucr.group1.query.Query;

import java.util.Queue;

import static ucr.group1.event.Event.eventType.*;

/**
 * Created by Daniel on 12/2/2017.
 */
public class Main {
    public static void main(String[]  args){
        int timeOut = 60;
        Simulation simulation = new Simulation(1,1,1,1,
                timeOut,false, 0);
        simulation.simulate();
        int idAsigner = 0;
        Event firstEvent = new Event(ENTER_CONNECTION,0, new Query(idAsigner++, simulation.getGenerator()));
        simulation.addEvent(firstEvent);
        while(true/* REVISAR CONDICIÓN DESPUÉS*/){
            Event actualEvent = simulation.getNextEvent();
            double exitTime;
            Query fromModule;
            switch(actualEvent.getEventType()){
                case ENTER_CONNECTION:
                    simulation.setTime(actualEvent.getTime());
                    exitTime = simulation.getConnection().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        simulation.addEvent(new Event(EXIT_CONNECTION, exitTime, actualEvent.getQuery()));
                        simulation.addEvent(new Event(KILL, timeOut + actualEvent.getTime(), actualEvent.getQuery()));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    simulation.addEvent(new Event(ENTER_CONNECTION, simulation.getTime() + simulation.getGenerator().getExponential(1.7142), new Query(idAsigner++, simulation.getGenerator())));
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_CONNECTION:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getConnection().aQueryFinished();
                    if(!fromModule.getDead()){
                        simulation.addEvent(new Event(ENTER_SYSTEMCALL, simulation.getTime(), fromModule));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case ENTER_SYSTEMCALL:
                    simulation.setTime(actualEvent.getTime());
                    exitTime = simulation.getSystemCall().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        simulation.addEvent(new Event(EXIT_SYSTEMCALL, exitTime, actualEvent.getQuery()));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_SYSTEMCALL:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getSystemCall().aQueryFinished();
                    if(!fromModule.getDead()){
                        simulation.addEvent(new Event(ENTER_VALIDATION, simulation.getTime(), fromModule));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    if(simulation.getSystemCall().isAQueryBeingServed()){
                        Query nextQueryToExit = simulation.getSystemCall().nextQueryToBeOut();
                        simulation.addEvent(new Event(EXIT_SYSTEMCALL, nextQueryToExit.getDepartureTime(), nextQueryToExit));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case ENTER_VALIDATION:
                    simulation.setTime(actualEvent.getTime());
                    exitTime = simulation.getValidation().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        simulation.addEvent(new Event(EXIT_VALIDATION, exitTime, actualEvent.getQuery()));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_VALIDATION:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getValidation().aQueryFinished();
                    if(!fromModule.getDead()){
                        simulation.addEvent(new Event(ENTER_VALIDATION, simulation.getTime(), fromModule));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    if(simulation.getValidation().isAQueryBeingServed()){
                        Query nextQueryToExit = simulation.getValidation().nextQueryToBeOut();
                        simulation.addEvent(new Event(EXIT_VALIDATION, nextQueryToExit.getDepartureTime(), nextQueryToExit));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case ENTER_STORAGE:
                    simulation.setTime(actualEvent.getTime());
                    exitTime = simulation.getStorage().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        simulation.addEvent(new Event(EXIT_STORAGE, exitTime, actualEvent.getQuery()));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_STORAGE:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getStorage().aQueryFinished();
                    if(!fromModule.getDead()){
                        simulation.addEvent(new Event(ENTER_EXECUTION, simulation.getTime(), fromModule));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    if(simulation.getValidation().isAQueryBeingServed()){
                        Query nextQueryToExit = simulation.getValidation().nextQueryToBeOut();
                        simulation.addEvent(new Event(EXIT_STORAGE, nextQueryToExit.getDepartureTime(), nextQueryToExit));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case ENTER_EXECUTION:
                    simulation.setTime(actualEvent.getTime());
                    exitTime = simulation.getExecution().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        simulation.addEvent(new Event(EXIT_EXECUTION, exitTime, actualEvent.getQuery()));
                    }
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_EXECUTION:
                    simulation.setTime(actualEvent.getTime());
                    fromModule = simulation.getStorage().aQueryFinished();
                    if(!fromModule.getDead()){
                        simulation.addEvent(new Event(ENTER_EXECUTION, simulation.getTime(), fromModule));
                    }
                    else{
                        // AQUI UNA CONSULTA MUERE Y AUMENTA LA ESTADíSTICA
                    }
                    if(simulation.getValidation().isAQueryBeingServed()){
                        Query nextQueryToExit = simulation.getValidation().nextQueryToBeOut();
                        simulation.addEvent(new Event(EXIT_STORAGE, nextQueryToExit.getDepartureTime(), nextQueryToExit));
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
                    }
                    break;
            }
        }
    }
}
