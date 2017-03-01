package ucr.group1.statistics;

import ucr.group1.module.Module;
import ucr.group1.query.Query;
import ucr.group1.query.QueryLabel;
import ucr.group1.simulation.Simulation;

import static ucr.group1.query.QueryLabel.*;

/**
 * Created by Daniel on 18/2/2017.
 */
public class ModuleStatistics {
    private double timeBetweenArrives;
    private int numberTimeBetweenArrives;
    private double lastArrive;
    private double l_s;
    private int numberL_S;
    private double l_q;
    private int numberL_Q;
    private double ddlMT;
    private int numberDdlMT;
    private double updateMT;
    private int numberUpdateMT;
    private double joinMT;
    private int numberJoinMT;
    private double selectMT;
    private int numberSelectMT;
    private Simulation simulation;
    private Module module;
    private int amountOfServedQueries;

    /**
     * Builds a new ModuleStatistics
     *
     * @param module     The module of the statistics
     * @param simulation A pointer to the simulation
     */
    public ModuleStatistics(Module module, Simulation simulation) {
        this.module = module;
        this.simulation = simulation;
        this.timeBetweenArrives = 0;
        this.numberTimeBetweenArrives = 0;
        this.lastArrive = 0;
        this.l_s = 0;
        this.numberL_S = 0;
        this.l_q = 0;
        this.numberL_Q = 0;
        this.ddlMT = 0;
        this.numberDdlMT = 0;
        this.updateMT = 0;
        this.numberUpdateMT = 0;
        this.selectMT = 0;
        this.numberSelectMT = 0;
        this.amountOfServedQueries = 0;
    }

    /**
     * @return The lambda of the module
     */
    public double getLambda() {
        return (1.0 / timeBetweenArrives);
    }

    /**
     * @return The mu of the module
     */
    public double getMu() {
        return (1.0 / getW_s());
    }

    /**
     * @return The rho of the module
     */
    public double getRho() {
        return (getW_s() / (module.getNumberOfServers() * timeBetweenArrives));
    }

    /**
     * @return The average amount of queries on module
     */
    public double getL() {
        return (getL_q() + getL_s());
    }

    /**
     * @return The average amount of queries that are being attended
     */
    public double getL_s() {
        return l_s;
    }

    /**
     * @return The average amount of queries that are on queue
     */
    public double getL_q() {
        return l_q;
    }

    /**
     * @return The average spended time of a single query on module
     */
    public double getW() {
        return (getW_q() + getW_s());
    }

    /**
     * @return The average spended time of a single query being served
     */
    public double getW_s() {
        return (getL_s() * timeBetweenArrives);
    }

    /**
     * @return The average spended time of a single query on queue
     */
    public double getW_q() {
        return (getL_q() * timeBetweenArrives);
    }

    /**
     * @param label The type of query that is wanted to get the average time through the module
     * @return The average time in module of a specified type of query
     */
    public double getAverageTime(QueryLabel label) {
        switch (label) {
            case DDL:
                return ddlMT;
            case JOIN:
                return joinMT;
            case SELECT:
                return selectMT;
            case UPDATE:
                return updateMT;
            default:
                return -1;
        }
    }

    /**
     * @return The leisure time of the module
     */
    public double getLeisureTime() {
        double rho = getRho();
        if (rho < 1) {
            return ((1.0 - rho) * simulation.getTime());
        } else {
            return 0;
        }
    }

    /**
     * @return The total of queries that passed through the module
     */
    public int getAmountOfServedQueries() {
        return amountOfServedQueries;
    }

    /**
     * @param newTimeBetweenArrives The time of a new arrival to the module
     */
    public void updateTimeBetweenArrives(double newTimeBetweenArrives) {
        numberTimeBetweenArrives++;
        double percentage = (1.0 - (1.0 / numberTimeBetweenArrives));
        timeBetweenArrives *= percentage;
        timeBetweenArrives += (1.0 - percentage) * (newTimeBetweenArrives - lastArrive);
        lastArrive = newTimeBetweenArrives;
    }

    /**
     * @param newL_q The size of the queue at a moment
     */
    public void updateL_Q(int newL_q) {
        numberL_Q++;
        double percentage = (1.0 - (1.0 / numberL_Q));
        l_q *= percentage;
        l_q += (1 - percentage) * (double) newL_q;
    }

    /**
     * @param newL_s The number of being served queries at a moment
     */
    public void updateL_S(int newL_s) {
        numberL_S++;
        double percentage = (1.0 - (1.0 / numberL_S));
        l_s *= percentage;
        l_s += (1 - percentage) * (double) newL_s;
    }

    /**
     * Updates the average of time passed to the module of a single type of query
     *
     * @param query         The query served by the module
     * @param newModuleTime The total time that the query passed through the module
     */
    public void updateModuleTime(Query query, double newModuleTime) {
        double percentage;
        switch (query.getType()) {
            case DDL:
                numberDdlMT++;
                percentage = (1.0 - (1.0 / numberDdlMT));
                ddlMT *= percentage;
                ddlMT += (1 - percentage) * newModuleTime;
                break;
            case UPDATE:
                numberUpdateMT++;
                percentage = (1.0 - (1.0 / numberUpdateMT));
                updateMT *= percentage;
                updateMT += (1.0 - percentage) * newModuleTime;
                break;
            case SELECT:
                numberSelectMT++;
                percentage = (1.0 - (1.0 / numberSelectMT));
                selectMT *= percentage;
                selectMT += (1.0 - percentage) * newModuleTime;
                break;
            case JOIN:
                numberJoinMT++;
                percentage = (1.0 - (1.0 / numberJoinMT));
                joinMT *= percentage;
                joinMT += (1.0 - percentage) * newModuleTime;
                break;
        }
        amountOfServedQueries++;
    }
}
