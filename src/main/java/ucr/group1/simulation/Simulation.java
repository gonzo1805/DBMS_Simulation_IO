package ucr.group1.simulation;
import ucr.group1.event.Event;
import ucr.group1.event.EventComparator;
import ucr.group1.generator.Generator;
import ucr.group1.module.*;
import ucr.group1.query.Query;
import ucr.group1.statistics.ModuleStatistics;
import ucr.group1.statistics.QueryStatistics;

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
    private QueryStatistics queryStatistics;
    private Generator generator;


    /**
     * Builds a new simulation
     * @param kConnections The number of servers in the Connection module
     * @param nConcurrentProcesses The number of servers in the Validation module
     * @param pTransactionProcesses The number of servers in the Execution module
     * @param mAvailableProcesses The number of servers in the Storage module
     * @param tTimeout The maximum time that a query can be in the system
     * @param slowMode A condition that activates the slow mode
     * @param timeBetweenEvents The time in seconds between the occurrence of events
     */
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
        if(this.slowMode){
            this.timeBetweenEvents = timeBetweenEvents;
        }
        else{
            this.timeBetweenEvents = 0;
        }
        this.generator = new Generator();
        buildModulesAndStatistics();
    }


    /********************************************** GETTERS ***********************************************************/

    /**
     * @return The generator of the simulation
     */
    public Generator getGenerator(){ return generator; }

    /**
     * @return The actual time in the simulation
     */
    public double getTime() {
        return time;
    }

    /**
     * @return The next event to occur
     */
    public Event getNextEvent() {
        return eventList.poll();
    }

    /**
     * @return The connection module
     */
    public Module getConnection() {
        return connection;
    }

    /**
     * @return The system call module
     */
    public Module getSystemCall() {
        return systemCall;
    }

    /**
     * @return The validation module
     */
    public Module getValidation() {
        return validation;
    }

    /**
     * @return The storage module
     */
    public Module getStorage() {
        return storage;
    }

    /**
     * @return The execution module
     */
    public Module getExecution() {
        return execution;
    }

    /**
     * @return The Statistics of the connection module
     */
    public ModuleStatistics getConnectionStatistics() {
        return connectionStatistics;
    }

    /**
     * @return The Statistics of the system call module
     */
    public ModuleStatistics getSystemCallStatistics() {
        return systemCallStatistics;
    }

    /**
     * @return The Statistics of the execution module
     */
    public ModuleStatistics getExecutionStatistics() {
        return executionStatistics;
    }

    /**
     * @return The Statistics of the storage module
     */
    public ModuleStatistics getStorageStatistics() {
        return storageStatistics;
    }

    /**
     * @return The Statistics of the validation module
     */
    public ModuleStatistics getValidationStatistics() {
        return validationStatistics;
    }

    /**
     * @return The Statistics of the queries
     */
    public QueryStatistics getQueryStatistics() {
        return queryStatistics;
    }

    /**
     * @param query The Query marked as killed
     * @return The queue that has the dead query
     */
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

    /**
     * @return A string with the actual time in the format: [HH:MM:SS]
     */
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

    /**
     *
     * @param time The new time
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Builds all the modules and the statistics
     */
    public void buildModulesAndStatistics(){
        this.connection = new Connection(kConnections,this,generator);
        this.systemCall = new SystemCall(this, generator);
        this.validation = new Validation(nConcurrentProcesses,this,generator);
        this.storage = new Storage(mAvailableProcesses,this,generator);
        this.execution = new Execution(pTransactionProcesses,this,generator);
        this.connectionStatistics = new ModuleStatistics(this.connection, this);
        this.systemCallStatistics = new ModuleStatistics(this.systemCall, this);
        this.validationStatistics = new ModuleStatistics(this.validation, this);
        this.storageStatistics = new ModuleStatistics(this.storage, this);
        this.executionStatistics = new ModuleStatistics(this.execution, this);
        this.queryStatistics = new QueryStatistics();
    }

    /**
     * @param event The event to add to the events list
     */
    public void addEvent(Event event){
        eventList.add(event);
    }

    /**
     * @param toFinalize The event to add in the finalized events list
     */
    public void finalizeEvent(Event toFinalize){
        finalizedEvents.add(toFinalize);
    }

    /**
     * @param query The query that was finished before reach his killing time
     */
    public void thisQueryKillNeverHappened(Query query){
        eventList.remove(query.getKillEvent());
    }

    /**
     * @param query The query that was killed before reach the next event assigned to it
     */
    public void thisQueryWereKilledBeforeReachTheNextEvent(Query query){
        eventList.remove(query.getNextEvent());
    }
}