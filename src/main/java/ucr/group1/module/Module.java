package ucr.group1.module;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import ucr.group1.query.*;

/**
 * Created by Gonzalo on 2/8/2017.
 */
public abstract class Module<E> {
    Queue<E> queue;
    int numberOfServers;
    int numberOfFreeServers;
    Queue<E> beignServedQuerys;
    List<Event> list;// De nuestra clase event, TODO hacer la clase event

    /**
     * Inserts a Query on queue if there is not free servers, if there are free servers, inserts a Query on
     * beignServedQuerys
     * @param query the Query to insert on queue
     */
    public abstract void entriesANewQuery(Query query);

    /**
     * Gets the next Query and inserts on beignServedQuerys
     */
    public abstract void aQueryIsServed();

    /**
     * Gets Query and pull out from the system
     */
    public abstract void rejectQuery(Query query);

    /**
     * Gets and pull the next Query from beingServedQueries
     * @return the next Query from beingServedQueries
     */
    public abstract Query aQueryFinished();

}
