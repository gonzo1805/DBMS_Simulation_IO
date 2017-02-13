package ucr.group1.simulation;

import ucr.group1.event.Event;
import ucr.group1.query.Query;

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
            switch(actualEvent.getEventType()){
                case ENTER_CONNECTION:
                    simulation.setTime(actualEvent.getTime());
                    double exitTime = simulation.getConnection().entriesANewQuery(actualEvent.getQuery());
                    if(exitTime > -1) {
                        simulation.addEvent(new Event(EXIT_CONNECTION, exitTime, actualEvent.getQuery()));
                        simulation.addEvent(new Event(KILL, timeOut + actualEvent.getTime(), actualEvent.getQuery()));
                    }
                    simulation.addEvent(new Event(ENTER_CONNECTION, simulation.getTime() + simulation.getGenerator().getExponential(1.7142), new Query(idAsigner++, simulation.getGenerator())));
                    simulation.finalizeEvent(actualEvent);
                    break;
                case EXIT_CONNECTION:
                    simulation.setTime(actualEvent.getTime());
                    Query fromConnection = simulation.getConnection().aQueryFinished();

                    simulation.finalizeEvent(actualEvent);
                    break;
                case ENTER_SYSTEMCALL:
                    break;
                case EXIT_SYSTEMCALL:
                    break;
                case ENTER_VALIDATION:
                    break;
                case EXIT_VALIDATION:
                    break;
                case ENTER_STORAGE:
                    break;
                case EXIT_STORAGE:
                    break;
                case ENTER_EXECUTION:
                    break;
                case EXIT_EXECUTION:
                    break;
                case KILL:
                    break;
            }
        }
    }
}
