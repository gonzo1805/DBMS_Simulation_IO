package ucr.group1.query;
import ucr.group1.event.Event;
import ucr.group1.generator.Generator;

/**
 * Created by Daniel on 8/2/2017.
 */
public class Query {
    private int id;
    private QueryType type;
    private double lifespan;
    private double departureTime;
    private double arrivalTime;
    private boolean dead;
    private boolean beingServed;
    private int chargedBlocks;

    public Query(int ID, Generator gener){
        id = ID;
        type = new QueryType(gener);
        lifespan = 0;
        departureTime = Double.MAX_VALUE;
        arrivalTime = Double.MAX_VALUE;
        dead = false;
        beingServed = false;
        chargedBlocks = 0;
    }

    /**
     * @param additive The amounts of seconds added to the lifespan
     */
    public void addLifeSpan(double additive) {
        this.lifespan += additive;
    }

    /**
     * Set on true the dead attribute of the query
     */
    public void kill() {
        this.dead = true;
    }


    /********************************************* GETTERS ***********************************************************/

    /**
     * @return The id of the query
     */
    public int getId() { return id; }

    /**
     * @return The priority of the query
     */
    public int getPriority(){
        return type.getPriority();
    }

    /**
     * @return True if and only if the query is read only
     */
    public boolean getReadOnly(){
        return type.getReadOnly();
    }

    /**
     * @return The type of the query
     */
    public QueryLabel getType() { return type.getType(); }

    /**
     * @return The lifespan of the query
     */
    public double getLifespan() {
        return lifespan;
    }

    /**
     * @return The last arrival time of the query
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @return The next departure time of the query
     */
    public double getDepartureTime() {
        return departureTime;
    }

    /**
     * @return True if and only if the query is dead
     */
    public boolean getDead() {
        return this.dead;
    }

    /**
     * @return The amount of charged blocks of the query
     */
    public int getChargedBlocks() {
        return chargedBlocks;
    }

    /**
     * @return True if and only if the query is being served
     */
    public boolean isBeingServed() {
        return beingServed;
    }


    /*********************************************** SETTERS **********************************************************/

    /**
     * @param arrivalTime The new arrival time of the query
     */
    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * @param departureTime The new departure time of the query
     */
    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * @param beingServed Set on his value the beingServed of the query
     */
    public void setBeingServed(boolean beingServed) {
        this.beingServed = beingServed;
    }

    /**
     * @param chargedBlocks The new amount of charged blocks of the query
     */
    public void setChargedBlocks(int chargedBlocks) {
        this.chargedBlocks = chargedBlocks;
    }
}