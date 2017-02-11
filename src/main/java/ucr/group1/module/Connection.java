package ucr.group1.module;

import ucr.group1.query.Query;

import java.awt.*;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Connection extends Module<Query> {

    public Connection(int numberOfFreeServers, Queue<Query> beingServedQueries, Queue<Event> eventList) {
        this.numberOfFreeServers = numberOfFreeServers;
        this.beingServedQueries = new PriorityQueue<Query>();
        this.eventList = eventList;
    }

    public void entriesANewQuery(Query query) {
        if (numberOfFreeServers > 0) {
            beingServedQueries.add(query);
           // getGenerator().getRandomUniform(0.01, 0.05);
        }
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
