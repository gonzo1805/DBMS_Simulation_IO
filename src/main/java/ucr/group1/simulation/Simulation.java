package ucr.group1.simulation;
import ucr.group1.event.Event;
import ucr.group1.event.EventComparator;
import ucr.group1.generator.Generator;
import ucr.group1.module.*;
import ucr.group1.query.Query;
import ucr.group1.statistics.ModuleStatistics;

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
    private ModuleStatistics connectionStatistics;
    private ModuleStatistics systemCallStatistics;
    private ModuleStatistics validationStatistics;
    private ModuleStatistics storageStatistics;
    private ModuleStatistics executionStatistics;
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
        buildModulesAndStatistics();
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

    public void buildModulesAndStatistics(){
        this.connection = new Connection(kConnections,this.eventList,this,generator);
        this.systemCall = new SystemCall(this, generator);
        this.validation = new Validation(nConcurrentProcesses,this,generator);
        this.storage = new Storage(mAvailableProcesses,this,generator);
        this.execution = new Execution(pTransactionProcesses,this,generator);
        this.connectionStatistics = new ModuleStatistics(this.connection, this);
        this.systemCallStatistics = new ModuleStatistics(this.systemCall, this);
        this.validationStatistics = new ModuleStatistics(this.validation, this);
        this.storageStatistics = new ModuleStatistics(this.storage, this);
        this.executionStatistics = new ModuleStatistics(this.execution, this);
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

    public ModuleStatistics getConnectionStatistics() {
        return connectionStatistics;
    }

    public ModuleStatistics getSystemCallStatistics() {
        return systemCallStatistics;
    }

    public ModuleStatistics getExecutionStatistics() {
        return executionStatistics;
    }

    public ModuleStatistics getStorageStatistics() {
        return storageStatistics;
    }

    public ModuleStatistics getValidationStatistics() {
        return validationStatistics;
    }

    public void finalizeEvent(Event toFinalize){
        finalizedEvents.add(toFinalize);
    }

    public void thisQueryKillNeverHappened(Query query){
        eventList.remove(query.getKillEvent());
    }

    public void thisQueryWereKilledBeforeReachTheNextEvent(Query query){
        eventList.remove(query.getNextEvent());
    }

    public Queue getDeadQueryQueue(Query query){
        if(systemCall.getQueue().contains(query)){
            return systemCall.getQueue();
        }
        else if(validation.getQueue().contains(query)){
            return validation.getQueue();
        }
        else if(storage.getQueue().contains(query)){
            return storage.getQueue();
        }
        else if(execution.getQueue().contains(query)){
            return execution.getQueue();
        }
        else{
            return null;
        }
    }

    public String getTimeInHHMMSS(){
        int seconds = (int)time;
        int minutes = (seconds/60);
        seconds -= (60*minutes);
        int hours = (minutes/60);
        minutes -= (60*hours);
        String secondsString = String.valueOf(seconds);
        String minutesString = String.valueOf(minutes);
        String hoursString = String.valueOf(hours);
        if(seconds < 10){
            secondsString = "0" + secondsString;
        }
        if(minutes < 10){
            minutesString = "0" + minutesString;
        }
        if(hours < 10){
            hoursString = "0" + hoursString;
        }
        return ("[" + hoursString + ":" + minutesString + ":" + secondsString + "] ");
    }

}