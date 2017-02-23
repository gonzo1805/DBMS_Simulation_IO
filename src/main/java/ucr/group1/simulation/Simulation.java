package ucr.group1.simulation;
import ucr.group1.event.Event;
import ucr.group1.event.EventComparator;
import ucr.group1.generator.Generator;
import ucr.group1.module.*;
import ucr.group1.query.Query;
import ucr.group1.statistics.QueryStatistics;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static ucr.group1.event.EventType.*;


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
    private /*Module*/ ClientManagementModule clientManagementModule;
    private /*Module*/ ProcessesManagementModule processesManagementModule;
    private /*Module*/ QueriesVerificationModule queriesVerificationModule;
    private /*Module*/ TransactionsModule transactionsModule;
    private /*Module*/ QueriesExecutionModule queriesExecutionModule;
    private QueryStatistics queryStatistics;
    private Generator generator;
    private List timeLog;
    private int timePerSimulation;


    /**
     * Builds a new simulation
     * @param kConnections The number of servers in the ClientManagementModule module
     * @param nConcurrentProcesses The number of servers in the QueriesVerificationModule module
     * @param pTransactionProcesses The number of servers in the QueriesExecutionModule module
     * @param mAvailableProcesses The number of servers in the TransactionsModule module
     * @param tTimeout The maximum time that a query can be in the system
     * @param slowMode A condition that activates the slow mode
     * @param timeBetweenEvents The time in seconds between the occurrence of events
     */
    public Simulation(int kConnections, int nConcurrentProcesses, int pTransactionProcesses, int mAvailableProcesses, int tTimeout, boolean slowMode, double timeBetweenEvents, int timePerSimulation) {
        time = 0;
        eventList = new PriorityQueue<Event>(1000000, new EventComparator());
        finalizedEvents = new LinkedList<Event>();
        this.kConnections = kConnections;
        this.nConcurrentProcesses = nConcurrentProcesses;
        this.pTransactionProcesses = pTransactionProcesses;
        this.mAvailableProcesses = mAvailableProcesses;
        this.tTimeout = tTimeout;
        this.slowMode = slowMode;
        if (this.slowMode) {
            this.timeBetweenEvents = timeBetweenEvents;
        } else {
            this.timeBetweenEvents = 0;
        }
        this.generator = new Generator();
        this.timeLog = new LinkedList<String>();
        this.timePerSimulation = timePerSimulation;
        buildModulesAndStatistics();
    }

    public void simulate() {
        int idAsigner = 1;

        Event firstEvent = new Event(A_NEW_QUERY_IS_REQUESTING ,0, new Query(idAsigner++, generator));

        addEvent(firstEvent);
        while (time < timePerSimulation) {
            Event actualEvent = getNextEvent();

            switch(actualEvent.getEventType()) {
                case A_NEW_QUERY_IS_REQUESTING:
                    clientManagementModule.newQueryRequestingEvent(idAsigner, actualEvent);

                    idAsigner++;
                    break;
                case RETURN_TO_CLIENT_MANAGEMENT_MODULE:
                    clientManagementModule.returnToClientManagementModuleEvent(actualEvent);
                    break;
                case A_QUERY_IS_FINISHED:
                    clientManagementModule.aQueryIsFinishedEvent(actualEvent);
                    break;
                case ENTER_PROCESSES_MANAGEMENT_MODULE:
                    processesManagementModule.enterProcessesManagementModule(actualEvent);
                    break;
                case EXIT_PROCESSES_MANAGEMENT_MODULE:
                    processesManagementModule.exitProcessesManagementModule(actualEvent);
                    break;
                case ENTER_VERIFICATION_MODULE:
                    queriesVerificationModule.enterVerificationModuleEvent(actualEvent);
                    break;
                case EXIT_VERIFICATION_MODULE:
                    queriesVerificationModule.exitVerificationModuleEvent(actualEvent);
                    break;
                case ENTER_TRANSACTIONS_MODULE:
                    transactionsModule.enterTransactionsModuleEvent(actualEvent);
                    break;
                case EXIT_TRANSACTIONS_MODULE:
                    transactionsModule.exitTransactionsModuleEvent(actualEvent);
                    break;
                case ENTER_EXECUTION_MODULE:
                    queriesExecutionModule.enterExecutionModuleEvent(actualEvent);
                    break;
                case EXIT_EXECUTION_MODULE:
                    queriesExecutionModule.exitExecutionModuleEvent(actualEvent);
                    break;
                case KILL:
                    time = actualEvent.getTime();
                    if (actualEvent.getQuery().isBeingServed()) {
                        actualEvent.getQuery().kill();
                    } else {
                        Queue queue = getDeadQueryQueue(actualEvent.getQuery());
                        if (queue != null) {
                            queue.remove(actualEvent.getQuery());
                        }
                        thisQueryWereKilledBeforeReachTheNextEvent(actualEvent.getQuery());
                        getQueryStatistics().rejectAQuery();
                    }
                    addLineInTimeLog("The query " + actualEvent.getQuery().getId() + " have reached his timeout, it will be kicked out");
                    break;
            }
            //
            try {
                Thread.sleep((long) timeBetweenEvents * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateAllTheLOfStatistics();
        }
    }

    public void addLineInTimeLog(String line){
        timeLog.add(getTimeInHHMMSS() + line);
    }

    /********************************************** GETTERS ***********************************************************/

    public int getTimeOut() {
        return tTimeout;
    }

    /**
     * @return The generator of the simulation
     */
    public Generator getGenerator() {
        return generator;
    }

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
     * @return The Statistics of the queries
     */
    public QueryStatistics getQueryStatistics() {
        return queryStatistics;
    }

    /**
     * @param query The Query marked as killed
     * @return The queue that has the dead query
     */
    public Queue getDeadQueryQueue(Query query) {
        if (processesManagementModule.getQueue().contains(query)) {
            return processesManagementModule.getQueue();
        } else if (queriesVerificationModule.getQueue().contains(query)) {
            return queriesVerificationModule.getQueue();
        } else if (transactionsModule.getQueue().contains(query)) {
            return transactionsModule.getQueue();
        } else if (queriesExecutionModule.getQueue().contains(query)) {
            return queriesExecutionModule.getQueue();
        } else {
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
     * @param time The new time
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Builds all the modules and the statistics
     */
    public void buildModulesAndStatistics() {
        this.clientManagementModule = new ClientManagementModule(kConnections, this, generator);
        this.processesManagementModule = new ProcessesManagementModule(this, generator);
        this.queriesVerificationModule = new QueriesVerificationModule(nConcurrentProcesses, this, generator);
        this.transactionsModule = new TransactionsModule(mAvailableProcesses, this, generator);
        this.queriesExecutionModule = new QueriesExecutionModule(pTransactionProcesses, this, generator);
        this.queryStatistics = new QueryStatistics();
    }

    /**
     * @param event The event to add to the events list
     */
    public void addEvent(Event event) {
        eventList.add(event);
    }

    /**
     * @param toFinalize The event to add in the finalized events list
     */
    public void finalizeEvent(Event toFinalize) {
        finalizedEvents.add(toFinalize);
    }

    /**
     * @param query The query that was finished before reach his killing time
     */
    public void thisQueryKillNeverHappened(Query query) {
        eventList.remove(query.getKillEvent());
    }

    /**
     * @param query The query that was killed before reach the next event assigned to it
     */
    public void thisQueryWereKilledBeforeReachTheNextEvent(Query query) {
        eventList.remove(query.getNextEvent());
    }

    public void releaseAConnectionServer() {
        clientManagementModule.releaseAServer();
    }

    public void updateAllTheLOfStatistics() {
        clientManagementModule.updateL_sStatistics();
        processesManagementModule.updateL_sStatistics();
        processesManagementModule.updateL_qStatistics();
        queriesVerificationModule.updateL_sStatistics();
        queriesVerificationModule.updateL_qStatistics();
        transactionsModule.updateL_sStatistics();
        transactionsModule.updateL_qStatistics();
        queriesExecutionModule.updateL_sStatistics();
        queriesExecutionModule.updateL_qStatistics();
    }

    public void createATimeLogArchive(String name) {
        name += ".txt";
        Path file = Paths.get(name);
        try {
            Files.write(file, timeLog, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}