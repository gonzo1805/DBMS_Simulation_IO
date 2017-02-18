package ucr.group1.query;
import ucr.group1.event.Event;
import ucr.group1.generator.Generator;

/**
 * Created by Daniel on 8/2/2017.
 */
public class Query {
    private QueryType type;
    private double lifespan;
    private double departureTime;
    private double arrivalTime;
    private int id;
    private double conectionDuration;
    private double systemCallDuration;
    private double validationDuration;
    private double storageDuration;
    private double executionDuration;
    private boolean dead;
    private boolean beingServed;
    private int chargedBlocks;
    private Event nextEvent;
    private Event killEvent;

    public Query(int ID, Generator gener){
        id = ID;
        type = new QueryType(gener);
        lifespan = 0;
        departureTime = Double.MAX_VALUE;
        arrivalTime = Double.MAX_VALUE;
        conectionDuration = 0;
        systemCallDuration = 0;
        validationDuration = 0;
        storageDuration = 0;
        executionDuration = 0;
        dead = false;
        beingServed = false;
        chargedBlocks = 0;
    }

    public int getId() { return id; }

    public int getPriority(){
        return type.getPriority();
    }

    public boolean getReadOnly(){
        return type.getReadOnly();
    }

    public QueryType.type getType() { return type.getType(); }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getConectionDuration() {
        return conectionDuration;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public double getExecutionDuration() {
        return executionDuration;
    }

    public double getLifespan() {
        return lifespan;
    }

    public double getStorageDuration() {
        return storageDuration;
    }

    public double getSystemCallDuration() {
        return systemCallDuration;
    }

    public double getValidationDuration() {
        return validationDuration;
    }

    public boolean getDead() {
        return this.dead;
    }

    public int getChargedBlocks() {
        return chargedBlocks;
    }

    public Event getKillEvent() {
        return killEvent;
    }

    public Event getNextEvent() {
        return nextEvent;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setConectionDuration(double conectionDuration) {
        this.lifespan += conectionDuration;
        this.conectionDuration += conectionDuration;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public void setExecutionDuration(double executionDuration) {
        this.lifespan += executionDuration;
        this.executionDuration = executionDuration;
    }

    public void setStorageDuration(double storageDuration) {
        this.lifespan += storageDuration;
        this.storageDuration = storageDuration;
    }

    public void setSystemCallDuration(double systemCallDuration) {
        this.lifespan += systemCallDuration;
        this.systemCallDuration = systemCallDuration;
    }

    /*
     *
     */
    public void setValidationDuration(double validationDuration) {
        this.lifespan += validationDuration;
        this.validationDuration = validationDuration;
    }

    public boolean isBeingServed() {
        return beingServed;
    }

    public void setBeingServed(boolean beingServed) {
        this.beingServed = beingServed;
    }

    public void setChargedBlocks(int chargedBlocks) {
        this.chargedBlocks = chargedBlocks;
    }

    public void setKillEvent(Event killEvent) {
        this.killEvent = killEvent;
    }

    public void setNextEvent(Event nextEvent) {
        this.nextEvent = nextEvent;
    }

    public void kill() {
        this.dead = true;
    }
}