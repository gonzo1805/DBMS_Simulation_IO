package ucr.group1.statistics;


import ucr.group1.query.Query;
import ucr.group1.query.QueryType;
import ucr.group1.module.ModuleType.*;

/**
 * Created by Daniel on 23/2/2017.
 */
public class SimulationsStatistics {
    private double[] lambda;
    private int[] numberLambda;
    private double[] mu;
    private int[] numberMu;
    private double[] l_s;
    private int[] numberL_S;
    private double[] l_q;
    private int[] numberL_Q;
    private double[] l;
    private int[] numberL;
    private double[] w_s;
    private int[] numberW_S;
    private double[] w_q;
    private int[] numberW_Q;
    private double[] w;
    private int[] numberW;
    private double[] rho;
    private int[] numberRho;
    private double[] leisureTime;
    private int[] numberLeisureTime;
    private double[] ddlMT;
    private int[] numberDdlMT;
    private double[] updateMT;
    private int[] numberUpdateMT;
    private double[] joinMT;
    private int[] numberJoinMT;
    private double[] selectMT;
    private int[] numberSelectMT;
    private double[] averageServedQueries;
    private int[] numberServedQueries;
    private double averageRejectedQueries;
    private int numberRejectedQueries;
    private double averageKilledQueries;
    private int numberKilledQueries;
    private double averageLifespan;
    private int numberAverageLifespan;

    public SimulationsStatistics(){
        lambda = new double[5];
        numberLambda = new int[5];
        mu = new double[5];
        numberMu = new int[5];
        l_s = new double[5];
        numberL_S = new int[5];
        l_q = new double[5];
        numberL_Q = new int[5];
        l = new double[5];
        numberL = new int[5];
        w_s = new double[5];
        numberW_S = new int[5];
        w_q = new double[5];
        numberW_Q = new int[5];
        w = new double[5];
        numberW = new int[5];
        ddlMT = new double[5];
        numberDdlMT = new int[5];
        updateMT = new double[5];
        numberDdlMT = new int[5];
        numberUpdateMT = new int[5];
        joinMT = new double[5];
        numberJoinMT = new int[5];
        selectMT = new double[5];
        numberSelectMT = new int[5];
        averageServedQueries = new double[5];
        numberServedQueries = new int[5];
        rho = new double[5];
        numberRho = new int[5];
        leisureTime = new double[5];
        numberLeisureTime = new int[5];
        for(int i = 0; i < 5; i++){
            lambda[i] = 0;
            numberLambda[i] = 0;
            mu[i] = 0;
            numberMu[i] = 0;
            l_s[i] = 0;
            numberL_S[i] = 0;
            l_q[i] = 0;
            numberL_Q[i] = 0;
            l[i] = 0;
            numberL[i] = 0;
            w_s[i] = 0;
            numberW_S[i] = 0;
            w_q[i] = 0;
            numberW_Q[i] = 0;
            w[i] = 0;
            numberW[i] = 0;
            ddlMT[i] = 0;
            numberDdlMT[i] = 0;
            updateMT[i] = 0;
            numberDdlMT[i] = 0;
            numberUpdateMT[i] = 0;
            joinMT[i] = 0;
            numberJoinMT[i] = 0;
            selectMT[i] = 0;
            numberSelectMT[i] = 0;
            averageServedQueries[i] = 0;
            numberServedQueries[i] = 0;
            rho[i] = 0;
            numberRho[i] = 0;
            leisureTime[i] = 0;
            numberLeisureTime[i] = 0;
        }
        averageRejectedQueries = 0;
        numberRejectedQueries = 0;
        averageKilledQueries = 0;
        numberKilledQueries = 0;
        averageLifespan = 0;
        numberAverageLifespan = 0;
    }

    public double getLambda(int nModule) {
        return lambda[nModule];
    }

    public double getMu(int nModule) {
        return mu[nModule];
    }

    public double getRho(int nModule) {
        return rho[nModule];
    }

    public double getL(int nModule) {
        return l[nModule];
    }

    public double getL_s(int nModule) {
        return l_s[nModule];
    }

    public double getL_q(int nModule) {
        return l_q[nModule];
    }

    public double getW(int nModule) {
        return w[nModule];
    }

    public double getW_s(int nModule) {
        return w_s[nModule];
    }

    public double getW_q(int nModule) {
        return w_q[nModule];
    }

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

    public double getLeisureTime(int nModule) {
        return leisureTime[nModule];
    }

    public double getAmountOfServedQueries(int nModule) {
        return averageServedQueries[nModule];
    }

    public double getAvgLifespanOfQuery(){
        return averageLifespan;
    }

    public double getNumberOfRejectedQueries(){
        return averageRejectedQueries;
    }

    public void updateLambda(double newLambda, int nModule) {
        lambda[nModule]++;
        double percentage = (1 - (1 / lambda[nModule]));
        lambda[nModule] *= percentage;
        lambda[nModule] += (1 - percentage) * newLambda;
    }

    public void updateL_Q(double newL_q, int nModule) {
        numberL_Q[nModule]++;
        double percentage = (1 - (1 / numberL_Q[nModule]));
        l_q[nModule] *= percentage;
        l_q[nModule] += (1 - percentage) * newL_q;
    }

    public void updateL_S(double newL_s, int nModule) {
        numberL_S[nModule]++;
        double percentage = (1 - (1 / numberL_S[nModule]));
        l_s[nModule] *= percentage;
        l_s[nModule] += (1 - percentage) * newL_s;
    }

    public void updateL(double newL, int nModule) {
        numberL[nModule]++;
        double percentage = (1 - (1 / numberL[nModule]));
        l[nModule] *= percentage;
        l[nModule] += (1 - percentage) * newL;
    }

    public void updateW_Q(double newW_q, int nModule) {
        numberW_Q[nModule]++;
        double percentage = (1 - (1 / numberW_Q[nModule]));
        w_q[nModule] *= percentage;
        w_q[nModule] += (1 - percentage) * newW_q;
    }

    public void updateW_S(double newW_s, int nModule) {
        numberW_S[nModule]++;
        double percentage = (1 - (1 / numberW_S[nModule]));
        w_s[nModule] *= percentage;
        w_s[nModule] += (1 - percentage) * newW_s;
    }

    public void updateW(double newW, int nModule) {
        numberW[nModule]++;
        double percentage = (1 - (1 / numberW[nModule]));
        w[nModule] *= percentage;
        w[nModule] += (1 - percentage) * newW;
    }

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
    }

    public void updateAverageServedQueries(double newServed, int nModule){
        numberServedQueries[nModule]++;
        double percentage = (1-(1/numberServedQueries[nModule]));
        averageServedQueries[nModule] *= percentage;
        averageServedQueries[nModule] += (1 - percentage)*newServed;
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

    public void updateAverageRejectedQueries(double newRejected){
        numberRejectedQueries++;
        double percentage = (1-(1/numberRejectedQueries));
        averageLifespan *= percentage;
        averageLifespan += (1 - percentage)*newRejected;
    }

    public void updateAverageKilledQueries(double newKilled){
        numberKilledQueries++;
        double percentage = (1-(1/numberKilledQueries));
        averageKilledQueries *= percentage;
        averageKilledQueries += (1 - percentage)*newKilled;
    }

    public void updateAverageLifespan(double newLifespan){
        numberAverageLifespan++;
        double percentage = (1-(1/numberAverageLifespan));
        averageLifespan *= percentage;
        averageLifespan += (1 - percentage)*newLifespan;
    }
}