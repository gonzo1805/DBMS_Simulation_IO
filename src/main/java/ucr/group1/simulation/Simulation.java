package ucr.group1.simulation;
import ucr.group1.event.Event;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Daniel on 11/2/2017.
 */
public class Simulation implements Comparator<Event>, Comparable<Event>{
    private double time;
    private Queue<Event> eventList;

    public Simulation(){
        time = 0;
        eventList = new PriorityQueue<Event>(Comparator<Event> comparator);
    }

    public int compareTo(Event o) {
        return (int)(0 - o.getTime());
    }

    public int compare(Event o1, Event o2) {
        return 0;
    }

}