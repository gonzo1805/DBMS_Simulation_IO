package ucr.group1.module;

import javax.management.Query;
import java.awt.*;
import java.util.List;
import java.util.Queue;

/**
 * Created by Gonzalo on 2/8/2017.
 */
public abstract class module<E> {
    Queue<E> queue;
    int numberOfServers;
    int numberOfFreeServers;
    Queue<E> beignServedQuerys;
    List<Event> list;// De nuestra clase event, TODO hacer la clase event

    /**
     * Inserts a query on queue if there is not free servers, if there are free servers, inserts a query on
     * beignServedQuerys
     * @param query the query to insert on queue
     */
    public abstract void entriesANewQuery(Query/*Nuestro Query TODO hacer la clase query*/ query);

    /**
     * Gets the next query and inserts on beignServedQuerys
     */
    public abstract void aQueryIsServed();

    /**
     * Gets query and pull out from the system
     */
    public abstract void rejectQuery(Query/*Nuestro Query TODO hacer la clase query*/ query);

    /**
     * Gets and pull the next query from beingServedQueries
     * @return the next query from beingServedQueries
     */
    public abstract Query/*Nuestro Query TODO hacer la clase query*/ aQueryFinished();

}
