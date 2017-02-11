package ucr.group1.module;

import ucr.group1.event.Event;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;

import java.awt.*;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;

import ucr.group1.event.*;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Connection extends Module<Query> {

    public Connection(int numberOfFreeServers, Queue<Query> beingServedQueries, Queue<Event> eventList, Simulation simulation) {
        this.numberOfFreeServers = numberOfFreeServers;
        this.beingServedQueries = new PriorityQueue<Query>();
        this.eventList = eventList;
        this.simulation = simulation;
    }

    public double entriesANewQuery(Query query) {
        if (numberOfFreeServers > 0) {
            query.setArrivalTime(simulation.getTime());
            eventList.add(new Event(Event.eventType.ENTER_CONNECTION, query.getArrivalTime(), query));
            beingServedQueries.add(query);
            query.setDepartureTime(getGenerator().getRandomUniform(0.01, 0.05) + query.getArrivalTime());
            numberOfFreeServers--;
        } else {

        }
        return 0;
    }

    public void aQueryIsServed() {

    }

    public void rejectQuery(Query query) {

    }

    public Query aQueryFinished() {
        return null;
    }

    public boolean confirmAliveQuery(Query query) {
        return false;
    }
}
