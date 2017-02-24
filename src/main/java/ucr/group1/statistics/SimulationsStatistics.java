package ucr.group1.statistics;


import ucr.group1.query.Query;
import ucr.group1.query.QueryType;

/**
 * Created by Daniel on 23/2/2017.
 */
public class SimulationsStatistics {
    private double[] timeBetweenArrives;
    private int[] numberTimeBetweenArrives;
    private double[] l_s;
    private int[] numberL_S;
    private double[] l_q;
    private int[] numberL_Q;
    private double[] ddlMT;
    private int[] numberDdlMT;
    private double[] updateMT;
    private int[] numberUpdateMT;
    private double[] joinMT;
    private int[] numberJoinMT;
    private double[] selectMT;
    private int[] numberSelectMT;
    private int[] amountOfServedQueries;
    private double[] rho;
    private int[] numberRho;
    private double[] leisureTime;
    private int[] numberLeisureTime;
    private int numberRejectedQueries;
    private double averageLifespan;
    private int numberAverageLifespan;

    public SimulationsStatistics(){
        timeBetweenArrives = new double[5];
        numberTimeBetweenArrives = new int[5];
        l_s = new double[5];
        numberL_S = new int[5];
        l_q = new double[5];
        numberL_Q = new int[5];
        ddlMT = new double[5];
        numberDdlMT = new int[5];
        updateMT = new double[5];
        numberDdlMT = new int[5];
        numberUpdateMT = new int[5];
        joinMT = new double[5];
        numberJoinMT = new int[5];
        selectMT = new double[5];
        numberSelectMT = new int[5];
        amountOfServedQueries = new int[5];
        rho = new double[5];
        numberRho = new int[5];
        leisureTime = new double[5];
        numberLeisureTime = new int[5];
        for(int i = 0; i < 5; i++){
            timeBetweenArrives[i] = 0;
            numberTimeBetweenArrives[i] = 0;
            l_s[i] = 0;
            numberL_S[i] = 0;
            l_q[i] = 0;
            numberL_Q[i] = 0;
            ddlMT[i] = 0;
            numberDdlMT[i] = 0;
            updateMT[i] = 0;
            numberDdlMT[i] = 0;
            numberUpdateMT[i] = 0;
            joinMT[i] = 0;
            numberJoinMT[i] = 0;
            selectMT[i] = 0;
            numberSelectMT[i] = 0;
            amountOfServedQueries[i] = 0;
            rho[i] = 0;
            numberRho[i] = 0;
            leisureTime[i] = 0;
            numberLeisureTime[i] = 0;
        }
        numberRejectedQueries = 0;
        averageLifespan = 0;
        numberAverageLifespan = 0;
    }

    /**
     * @return The lambda of the module
     */
    public double getLambda(int nModule) {
        return (1 / timeBetweenArrives[nModule]);
    }

    /**
     * @return The mu of the module
     */
    public double getMu(int nModule) {
        return (1 / getW_s(nModule));
    }

    /**
     * @return The rho of the module
     */
    public double getRho(int nModule) {
        return rho[nModule];
    }

    /**
     * @return The average amount of queries on module
     */
    public double getL(int nModule) {
        return (getL_q(nModule) + getL_s(nModule));
    }

    /**
     * @return The average amount of queries that are being attended
     */
    public double getL_s(int nModule) {
        return l_s[nModule];
    }

    /**
     * @return The average amount of queries that are on queue
     */
    public double getL_q(int nModule) {
        return l_q[nModule];
    }

    /**
     * @return The average spended time of a single query on module
     */
    public double getW(int nModule) {
        return (getL(nModule) * timeBetweenArrives[nModule]);
    }

    /**
     * @return The average spended time of a single query being served
     */
    public double getW_s(int nModule) {
        return (getL_s(nModule) * timeBetweenArrives[nModule]);
    }

    /**
     * @return The average spended time of a single query on queue
     */
    public double getW_q(int nModule) {
        return (getL_q(nModule) * timeBetweenArrives[nModule]);
    }

    /**
     * @param type The type of query that is wanted to get the average time through the module
     * @return The average time in module of a specified type of query
     */
    public double getAverageTime(QueryType type, int nModule) {
        switch (type.getType()) {
            case DDL:
                return ddlMT[nModule];
            case JOIN:
                return joinMT[nModule];
            case SELECT:
                return selectMT[nModule];
            case UPDATE:
                return updateMT[nModule];
            default:
                return -1;
        }
    }

    /**
     * @return The leisure time of the module
     */
    public double getLeisureTime(int nModule) {
        return leisureTime[nModule];
    }

    /**
     * @return The total of queries that passed through the module
     */
    public int getAmountOfServedQueries(int nModule) {
        return amountOfServedQueries[nModule];
    }

    /**
     * @param newTimeBetweenArrives The time of a new arrival to the module
     */
    public void updateTimeBetweenArrives(double newTimeBetweenArrives, int nModule) {
        numberTimeBetweenArrives[nModule]++;
        double percentage = (1 - (1 / numberTimeBetweenArrives[nModule]));
        timeBetweenArrives[nModule] *= percentage;
        timeBetweenArrives[nModule] += (1 - percentage) * newTimeBetweenArrives;
    }

    /**
     * @param newL_q The size of the queue at a moment
     */
    public void updateL_Q(int newL_q, int nModule) {
        numberL_Q[nModule]++;
        double percentage = (1 - (1 / numberL_Q[nModule]));
        l_q[nModule] *= percentage;
        l_q[nModule] += (1 - percentage) * (double) newL_q;
    }

    /**
     * @param newL_s The number of being served queries at a moment
     */
    public void updateL_S(int newL_s, int nModule) {
        numberL_S[nModule]++;
        double percentage = (1 - (1 / numberL_S[nModule]));
        l_s[nModule] *= percentage;
        l_s[nModule] += (1 - percentage) * (double) newL_s;
    }

    /**
     * Updates the average of time passed to the module of a single type of query
     *
     * @param queryType         The query served by the module
     * @param newModuleTime The total time that the query passed through the module
     */
    public void updateModuleTime(QueryType queryType, double newModuleTime, int nModule) {
        double percentage;
        switch (queryType.getType()) {
            case DDL:
                numberDdlMT[nModule]++;
                percentage = (1 - (1 / numberDdlMT[nModule]));
                ddlMT[nModule] *= percentage;
                ddlMT[nModule] += (1 - percentage) * newModuleTime;
                break;
            case UPDATE:
                numberUpdateMT[nModule]++;
                percentage = (1 - (1 / numberUpdateMT[nModule]));
                updateMT[nModule] *= percentage;
                updateMT[nModule] += (1 - percentage) * newModuleTime;
                break;
            case SELECT:
                numberSelectMT[nModule]++;
                percentage = (1 - (1 / numberSelectMT[nModule]));
                selectMT[nModule] *= percentage;
                selectMT[nModule] += (1 - percentage) * newModuleTime;
                break;
            case JOIN:
                numberJoinMT[nModule]++;
                percentage = (1 - (1 / numberJoinMT[nModule]));
                joinMT[nModule] *= percentage;
                joinMT[nModule] += (1 - percentage) * newModuleTime;
                break;
        }
        amountOfServedQueries[nModule]++;
    }

    public void updateRho(double newRho, int nModule){
        numberRho[nModule]++;
        double percentage = (1 - (1 / numberRho[nModule]));
        rho[nModule] *= percentage;
        rho[nModule] += (1 - percentage) * newRho;
    }

    public void updateLeisureTime(double newLeisureTime, int nModule){
        numberLeisureTime[nModule]++;
        double percentage = (1 - (1 / numberLeisureTime[nModule]));
        leisureTime[nModule] *= percentage;
        leisureTime[nModule] += (1 - percentage) * newLeisureTime;
    }

    public double getAvgLifespanOfQuery(){
        return averageLifespan;
    }

    public int getNumberOfRejectedQueries(){
        return numberRejectedQueries;
    }

    public void rejectAQuery(){
        numberRejectedQueries++;
    }

    public void addFinishedQuery(Query q){
        numberAverageLifespan++;
        double percentage = (numberAverageLifespan-1)/(numberAverageLifespan);
        averageLifespan *= percentage;
        averageLifespan += (1 - percentage)*q.getLifespan();
    }
}