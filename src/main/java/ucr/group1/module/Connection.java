package ucr.group1.module;

import ucr.group1.query.Query;

import java.awt.*;
import java.util.Queue;
import java.util.concurrent.locks.Condition;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Connection extends Module<Query> {

    public Connection(int numberOfFreeServers, int numberOfServers, Queue<Query> queue, Queue<Query> beignServedQuerys, Queue<Event> eventList) {
        super(numberOfFreeServers, numberOfServers, queue, beignServedQuerys, eventList);
    }

    public void entriesANewQuery(Query query) {

    }

    public void aQueryIsServed() {

    }

    public void rejectQuery(Query query) {

    }

    public Query aQueryFinished() {
        return null;
    }
}
