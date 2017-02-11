package ucr.group1.simulation;
import ucr.group1.event.Event;
import ucr.group1.event.EventComparator;

import java.util.*;

/**
 * Created by Daniel on 11/2/2017.
 */
public class Simulation {

    private double time;
    private Queue<Event> eventList;
    private List<Event> finalizedEvents;
    private int kConnections;
    private int nConcurrentProcesses;
    private int pTransactionProcesses;
    private int mAvailableProcesses;
    private int tTimeout;
    private boolean slowMode;
    private double timeBetweenEvents;


    public Simulation(){
        time = 0;
        eventList = new PriorityQueue<Event>(1000000, new EventComparator());
        finalizedEvents = new LinkedList<Event>();
    }


}