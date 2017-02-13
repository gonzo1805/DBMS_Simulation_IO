package ucr.group1.simulation;
import ucr.group1.event.Event;
import ucr.group1.event.EventComparator;
import ucr.group1.generator.Generator;
import ucr.group1.module.*;

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
    private Module connection;
    private Module systemCall;
    private Module validation;
    private Module storage;
    private Module execution;
    private Generator generator;


    public Simulation(int kConnections, int nConcurrentProcesses, int pTransactionProcesses, int mAvailableProcesses, int tTimeout, boolean slowMode, double timeBetweenEvents) {
        time = 0;
        eventList = new PriorityQueue<Event>(1000000, new EventComparator());
        finalizedEvents = new LinkedList<Event>();
        this.kConnections = kConnections;
        this.nConcurrentProcesses = nConcurrentProcesses;
        this.pTransactionProcesses = pTransactionProcesses;
        this.mAvailableProcesses = mAvailableProcesses;
        this.tTimeout = tTimeout;
        this.slowMode = slowMode;
        this.timeBetweenEvents = timeBetweenEvents;
        this.generator = new Generator();
        buildModules();
    }

    public void simulate(){
        while(time < 15000){

        }
    }

    public Generator getGenerator(){ return generator; }

    public double getTime() {
        return time;
    }

    public Event getNextEvent() {
        return eventList.poll();
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void buildModules(){
        this.connection = new Connection(kConnections,this.eventList,this,generator);
        this.systemCall = new SystemCall(this, generator);
        this.validation = new Validation(nConcurrentProcesses,this,generator);
        this.storage = new Storage(mAvailableProcesses,this,generator);
        this.execution = new Execution(pTransactionProcesses,this,generator);
    }

    public void addEvent(Event event){
        eventList.add(event);
    }

    public Module getConnection() {
        return connection;
    }

    public Module getSystemCall() {
        return systemCall;
    }

    public Module getValidation() {
        return validation;
    }

    public Module getStorage() {
        return storage;
    }

    public Module getExecution() {
        return execution;
    }

    public void finalizeEvent(Event toFinalize){
        finalizedEvents.add(toFinalize);
    }
}