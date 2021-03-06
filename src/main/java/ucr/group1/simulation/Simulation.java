package ucr.group1.simulation;
import ucr.group1.event.Event;
import ucr.group1.event.EventComparator;
import ucr.group1.generator.Generator;
import ucr.group1.module.*;
import ucr.group1.query.Query;
import ucr.group1.statistics.ModuleStatistics;
import ucr.group1.statistics.QueryStatistics;
import ucr.group1.ui.Controller;
import ucr.group1.ui.Printer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import static ucr.group1.event.EventType.*;


/**
 * Created by Daniel and Gonzalo on 11/2/2017.
 */
public class Simulation {

    /**
     * Attributes
     */
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
    private ClientManagementModule clientManagementModule;
    private ProcessesManagementModule processesManagementModule;
    private QueriesVerificationModule queriesVerificationModule;
    private TransactionsModule transactionsModule;
    private QueriesExecutionModule queriesExecutionModule;
    private QueryStatistics queryStatistics;
    private Generator generator;
    private Controller controller;
    private List<String> timeLog;
    private Queue<String> timeLogAux;
    private int timePerSimulation;
    private HashMap<Query, Event> killsMap;


    /**
     * Builds a new simulation
     *
     * @param kConnections          The number of servers in the ClientManagementModule module
     * @param nConcurrentProcesses  The number of servers in the QueriesVerificationModule module
     * @param pTransactionProcesses The number of servers in the QueriesExecutionModule module
     * @param mAvailableProcesses   The number of servers in the TransactionsModule module
     * @param tTimeout              The maximum time that a query can be in the system
     * @param slowMode              A condition that activates the slow mode
     * @param timeBetweenEvents     The time in seconds between the occurrence of events
     */
    public Simulation(int kConnections, int nConcurrentProcesses, int pTransactionProcesses, int mAvailableProcesses,
                      int tTimeout, boolean slowMode, double timeBetweenEvents, int timePerSimulation,
                      Controller controller) {
        time = 0;
        eventList = new PriorityQueue<Event>(kConnections * 2, new EventComparator());
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
        this.controller = controller;
        this.timeLog = new LinkedList<String>();
        this.timeLogAux = new LinkedBlockingDeque<>(5);
        this.timePerSimulation = timePerSimulation;
        this.killsMap = new HashMap<>(kConnections);
        buildModulesAndStatistics();
    }

    /**
     * Add a line on a timeLog for debug purposes
     *
     * @param line
     */
    public void addLineInTimeLog(String line) {
        line = getTimeInHHMMSS() + line;
        timeLog.add(line);
        if (slowMode) {
            timeLogAux.add(line);
        }
    }

    /**
     * Process the Next Event
     *
     * @param idAssigner the number of the event
     */
    public void processNextEvent(int idAssigner) {
        Event actualEvent = getNextEvent();
        this.time = actualEvent.getTime();
        switch (actualEvent.getEventType()) {// What type of event is
            case A_NEW_QUERY_IS_REQUESTING:
                clientManagementModule.newQueryRequestingEvent(idAssigner, actualEvent);
                idAssigner++;
                break;
            case A_QUERY_IS_FINISHED:
                clientManagementModule.aQueryIsFinishedEvent(actualEvent);
                break;
            case EXIT_CLIENT_MANAGEMENT_MODULE:
                clientManagementModule.exitClientManagementEvent(actualEvent);
                break;
            case EXIT_PROCESSES_MANAGEMENT_MODULE:
                processesManagementModule.exitProcessesManagementModule(actualEvent);
                break;
            case EXIT_VERIFICATION_MODULE:
                queriesVerificationModule.exitVerificationModuleEvent(actualEvent);
                break;
            case EXIT_TRANSACTIONS_MODULE:
                transactionsModule.exitTransactionsModuleEvent(actualEvent);
                break;
            case EXIT_EXECUTION_MODULE:
                queriesExecutionModule.exitExecutionModuleEvent(actualEvent);
                break;
            case KILL:
                if (actualEvent.getQuery().isBeingServed()) {
                    actualEvent.getQuery().kill();
                } else {
                    kickTheQueryFromAQueue(actualEvent.getQuery());
                    getQueryStatistics().aQueryIsKilled();
                }
                addLineInTimeLog("The query " + actualEvent.getQuery().getId() + " have reached his timeout, " +
                        "it will be kicked out");
                break;
        }
        // Create the Printer object to send it to the UI for its print, on slowMode == true
        if (slowMode) {
            String toWrite = "";
            while (!timeLogAux.isEmpty()) {
                toWrite = toWrite + timeLogAux.poll() + "\n";
            }
            controller.updateTextArea(new Printer((int) time, String.valueOf(actualEvent.getEventType()), toWrite));
        }
        updateAllTheLOfStatistics();
        finalizeEvent(actualEvent);
    }

    /********************************************** GETTERS ***********************************************************/

    public int getNumberBusyServersOnClientManagementModule() {
        return clientManagementModule.getNumberOfBusyServers();
    }

    public int getTimePerSimulation() {
        return timePerSimulation;
    }

    public int getNumberBusyServersOnProcessesManagementModule() {
        return processesManagementModule.getNumberOfBusyServers();
    }

    public int getNumberBusyServersOnQueriesVerificationModule() {
        return queriesVerificationModule.getNumberOfBusyServers();
    }

    public int getNumberBusyServersOnTransactionsModule() {
        return transactionsModule.getNumberOfBusyServers();
    }

    public int getNumberBusyServersOnQueriesExecutionModule() {
        return queriesExecutionModule.getNumberOfBusyServers();
    }

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
     * Removes the query
     *
     * @param query The Query
     */
    public void kickTheQueryFromAQueue(Query query) {
        if (processesManagementModule.kickAQueryFromQueue(query)) {
        } else if (queriesVerificationModule.kickAQueryFromQueue(query)) {
        } else if (transactionsModule.kickAQueryFromQueue(query)) {
        } else if (queriesExecutionModule.kickAQueryFromQueue(query)) {
        }
    }

    /**
     * @return A string with the actual time in the format: [HH:MM:SS]
     */
    public String getTimeInHHMMSS() {
        int seconds = (int) time;
        int minutes = (seconds / 60);
        seconds -= (60 * minutes);
        int hours = (minutes / 60);
        minutes -= (60 * hours);
        String secondsString = String.valueOf(seconds);
        String minutesString = String.valueOf(minutes);
        String hoursString = String.valueOf(hours);
        if (seconds < 10) {
            secondsString = "0" + secondsString;
        }
        if (minutes < 10) {
            minutesString = "0" + minutesString;
        }
        if (hours < 10) {
            hoursString = "0" + hoursString;
        }
        return ("[" + hoursString + ":" + minutesString + ":" + secondsString + "] ");
    }

    /**
     * Builds all the modules and the statistics
     */
    public void buildModulesAndStatistics() {
        this.clientManagementModule = new ClientManagementModule(kConnections, this, generator);
        this.queriesExecutionModule = new QueriesExecutionModule(pTransactionProcesses, this, generator, this.clientManagementModule);
        this.transactionsModule = new TransactionsModule(mAvailableProcesses, this, generator, this.queriesExecutionModule);
        this.queriesVerificationModule = new QueriesVerificationModule(nConcurrentProcesses, this, generator, this.transactionsModule);
        this.processesManagementModule = new ProcessesManagementModule(this, generator, this.queriesVerificationModule);
        this.clientManagementModule.setNextModule(processesManagementModule);
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

    public void releaseAConnectionServer() {
        clientManagementModule.releaseAServer();
    }

    public void addKillEventToMap(Query query, Event killEvent) {
        killsMap.put(query, killEvent);
    }

    /**
     * @param query The query that was finished before reach his killing time
     */
    public void removeKillEventFromList(Query query) {
        Event killEvent = killsMap.remove(query);
        eventList.remove(killEvent);
    }

    public ModuleStatistics getClientManagementStatistics(){
        return clientManagementModule.getStatistics();
    }

    public ModuleStatistics getProcessesManagementStatistics(){
        return processesManagementModule.getStatistics();
    }

    public ModuleStatistics getQueriesVerificationStatistics(){
        return queriesVerificationModule.getStatistics();
    }

    public ModuleStatistics getTransactionsStatistics(){
        return transactionsModule.getStatistics();
    }

    public ModuleStatistics getQueriesExecutionStatistics(){
        return queriesExecutionModule.getStatistics();
    }

    /**
     * Update all the statistics
     */
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

    /**
     * Creates a time log for debug purposes, right now it is disabled
     * @param name the name of the time log
     */
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