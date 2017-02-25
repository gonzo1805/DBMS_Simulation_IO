package ucr.group1.statistics;


import ucr.group1.module.ModuleType.*;
import ucr.group1.query.QueryLabel;
import ucr.group1.simulation.Simulation;

import static ucr.group1.query.QueryLabel.*;

/**
 * Created by Daniel on 23/2/2017.
 */
public class SimulationsStatistics {
    private double[] lambda;
    private double[] mu;
    private double[] l_s;
    private double[] l_q;
    private double[] l;
    private double[] w_s;
    private double[] w_q;
    private double[] w;
    private double[] rho;
    private double[] leisureTime;
    private double[] ddlMT;
    private double[] updateMT;
    private double[] joinMT;
    private double[] selectMT;
    private double[] averageServedQueries;
    private double averageRejectedQueries;
    private double averageKilledQueries;
    private double averageLifespan;
    private int totalSimulations;

    public SimulationsStatistics(){
        lambda = new double[5];
        mu = new double[5];
        l_s = new double[5];
        l_q = new double[5];
        l = new double[5];
        w_s = new double[5];
        w_q = new double[5];
        w = new double[5];
        ddlMT = new double[5];
        updateMT = new double[5];
        joinMT = new double[5];
        selectMT = new double[5];
        averageServedQueries = new double[5];
        rho = new double[5];
        leisureTime = new double[5];
        for(int i = 0; i < 5; i++){
            lambda[i] = 0;
            mu[i] = 0;
            l_s[i] = 0;
            l_q[i] = 0;
            l[i] = 0;
            w_s[i] = 0;
            w_q[i] = 0;
            w[i] = 0;
            ddlMT[i] = 0;
            updateMT[i] = 0;
            joinMT[i] = 0;
            selectMT[i] = 0;
            averageServedQueries[i] = 0;
            rho[i] = 0;
            leisureTime[i] = 0;
        }
        averageRejectedQueries = 0;
        averageKilledQueries = 0;
        averageLifespan = 0;
        totalSimulations = 0;
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

    public double getAverageTime(QueryLabel label, int nModule) {
        switch (label) {
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

    public double getAverageLifespan(){
        return averageLifespan;
    }

    public double getAverageRejectedQueries(){
        return averageRejectedQueries;
    }

    public double getAverageKilledQueries(){
        return averageKilledQueries;
    }

    private void updateLambda(double newLambda, int nModule) {
        double percentage = (1-(1/totalSimulations));
        lambda[nModule] *= percentage;
        lambda[nModule] += (1 - percentage) * newLambda;
    }

    private void updateMu(double newMu, int nModule) {
        double percentage = (1-(1/totalSimulations));
        lambda[nModule] *= percentage;
        lambda[nModule] += (1 - percentage) * newMu;
    }

    private void updateL_Q(double newL_q, int nModule) {
        double percentage = (1-(1/totalSimulations));
        l_q[nModule] *= percentage;
        l_q[nModule] += (1 - percentage) * newL_q;
    }

    private void updateL_S(double newL_s, int nModule) {
        double percentage = (1-(1/totalSimulations));
        l_s[nModule] *= percentage;
        l_s[nModule] += (1 - percentage) * newL_s;
    }

    private void updateL(double newL, int nModule) {
        double percentage = (1-(1/totalSimulations));
        l[nModule] *= percentage;
        l[nModule] += (1 - percentage) * newL;
    }

    private void updateW_Q(double newW_q, int nModule) {
        double percentage = (1-(1/totalSimulations));
        w_q[nModule] *= percentage;
        w_q[nModule] += (1 - percentage) * newW_q;
    }

    private void updateW_S(double newW_s, int nModule) {
        double percentage = (1-(1/totalSimulations));
        w_s[nModule] *= percentage;
        w_s[nModule] += (1 - percentage) * newW_s;
    }

    private void updateW(double newW, int nModule) {
        double percentage = (1-(1/totalSimulations));
        w[nModule] *= percentage;
        w[nModule] += (1 - percentage) * newW;
    }

    private void updateModuleTime(QueryLabel label, double newModuleTime, int nModule) {
        double percentage;
        switch (label) {
            case DDL:
                percentage = (1-(1/totalSimulations));
                ddlMT[nModule] *= percentage;
                ddlMT[nModule] += (1 - percentage) * newModuleTime;
                break;
            case UPDATE:
                percentage = (1-(1/totalSimulations));
                updateMT[nModule] *= percentage;
                updateMT[nModule] += (1 - percentage) * newModuleTime;
                break;
            case SELECT:
                percentage = (1-(1/totalSimulations));
                selectMT[nModule] *= percentage;
                selectMT[nModule] += (1 - percentage) * newModuleTime;
                break;
            case JOIN:
                percentage = (1-(1/totalSimulations));
                joinMT[nModule] *= percentage;
                joinMT[nModule] += (1 - percentage) * newModuleTime;
                break;
        }
    }

    private void updateAverageServedQueries(double newServed, int nModule){
        double percentage = (1-(1/totalSimulations));
        averageServedQueries[nModule] *= percentage;
        averageServedQueries[nModule] += (1 - percentage)*newServed;
    }

    private void updateRho(double newRho, int nModule){
        double percentage = (1-(1/totalSimulations));
        rho[nModule] *= percentage;
        rho[nModule] += (1 - percentage) * newRho;
    }

    private void updateLeisureTime(double newLeisureTime, int nModule){
        double percentage = (1-(1/totalSimulations));
        leisureTime[nModule] *= percentage;
        leisureTime[nModule] += (1 - percentage) * newLeisureTime;
    }

    public void updateAverageRejectedQueries(double newRejected){
        double percentage = (1-(1/totalSimulations));
        averageLifespan *= percentage;
        averageLifespan += (1 - percentage)*newRejected;
    }

    public void updateAverageKilledQueries(double newKilled){
        double percentage = (1-(1/totalSimulations));
        averageKilledQueries *= percentage;
        averageKilledQueries += (1 - percentage)*newKilled;
    }

    public void updateAverageLifespan(double newLifespan){
        double percentage = (1-(1/totalSimulations));
        averageLifespan *= percentage;
        averageLifespan += (1 - percentage)*newLifespan;
    }

    public void addSimulation(Simulation simulation){
        totalSimulations++;
        //CLIENT MANAGEMENT MODULE
        updateLambda(simulation.getClientManagementStatistics().getLambda(),0);
        updateMu(simulation.getClientManagementStatistics().getMu(),0);
        updateL_S(simulation.getClientManagementStatistics().getL_s(),0);
        updateL_Q(simulation.getClientManagementStatistics().getL_q(),0);
        updateL(simulation.getClientManagementStatistics().getL(),0);
        updateW_S(simulation.getClientManagementStatistics().getW_s(),0);
        updateW_Q(simulation.getClientManagementStatistics().getW_q(),0);
        updateW(simulation.getClientManagementStatistics().getW(),0);
        updateRho(simulation.getClientManagementStatistics().getRho(),0);
        updateLeisureTime(simulation.getClientManagementStatistics().getLeisureTime(),0);
        updateModuleTime(DDL,simulation.getClientManagementStatistics().getAverageTime(DDL),0);
        updateModuleTime(UPDATE,simulation.getClientManagementStatistics().getAverageTime(UPDATE),0);
        updateModuleTime(SELECT,simulation.getClientManagementStatistics().getAverageTime(SELECT),0);
        updateModuleTime(JOIN,simulation.getClientManagementStatistics().getAverageTime(JOIN),0);
        updateAverageServedQueries(simulation.getClientManagementStatistics().getAmountOfServedQueries(),0);
        //PROCESSES MANAGEMENT MODULE
        updateLambda(simulation.getProcessesManagementStatistics().getLambda(),1);
        updateMu(simulation.getProcessesManagementStatistics().getMu(),1);
        updateL_S(simulation.getProcessesManagementStatistics().getL_s(),1);
        updateL_Q(simulation.getProcessesManagementStatistics().getL_q(),1);
        updateL(simulation.getProcessesManagementStatistics().getL(),1);
        updateW_S(simulation.getProcessesManagementStatistics().getW_s(),1);
        updateW_Q(simulation.getProcessesManagementStatistics().getW_q(),1);
        updateW(simulation.getProcessesManagementStatistics().getW(),1);
        updateRho(simulation.getProcessesManagementStatistics().getRho(),1);
        updateLeisureTime(simulation.getProcessesManagementStatistics().getLeisureTime(),1);
        updateModuleTime(DDL,simulation.getProcessesManagementStatistics().getAverageTime(DDL),1);
        updateModuleTime(UPDATE,simulation.getProcessesManagementStatistics().getAverageTime(UPDATE),1);
        updateModuleTime(SELECT,simulation.getProcessesManagementStatistics().getAverageTime(SELECT),1);
        updateModuleTime(JOIN,simulation.getProcessesManagementStatistics().getAverageTime(JOIN),1);
        updateAverageServedQueries(simulation.getProcessesManagementStatistics().getAmountOfServedQueries(),1);
        //QUERIES VERIFICATION MODULE
        updateLambda(simulation.getQueriesVerificationStatistics().getLambda(),2);
        updateMu(simulation.getQueriesVerificationStatistics().getMu(),2);
        updateL_S(simulation.getQueriesVerificationStatistics().getL_s(),2);
        updateL_Q(simulation.getQueriesVerificationStatistics().getL_q(),2);
        updateL(simulation.getQueriesVerificationStatistics().getL(),2);
        updateW_S(simulation.getQueriesVerificationStatistics().getW_s(),2);
        updateW_Q(simulation.getQueriesVerificationStatistics().getW_q(),2);
        updateW(simulation.getQueriesVerificationStatistics().getW(),2);
        updateRho(simulation.getQueriesVerificationStatistics().getRho(),2);
        updateLeisureTime(simulation.getQueriesVerificationStatistics().getLeisureTime(),2);
        updateModuleTime(DDL,simulation.getQueriesVerificationStatistics().getAverageTime(DDL),2);
        updateModuleTime(UPDATE,simulation.getQueriesVerificationStatistics().getAverageTime(UPDATE),2);
        updateModuleTime(SELECT,simulation.getQueriesVerificationStatistics().getAverageTime(SELECT),2);
        updateModuleTime(JOIN,simulation.getQueriesVerificationStatistics().getAverageTime(JOIN),2);
        updateAverageServedQueries(simulation.getQueriesVerificationStatistics().getAmountOfServedQueries(),2);
        //TRANSACTIONS MODULE
        updateLambda(simulation.getTransactionsStatistics().getLambda(),3);
        updateMu(simulation.getTransactionsStatistics().getMu(),3);
        updateL_S(simulation.getTransactionsStatistics().getL_s(),3);
        updateL_Q(simulation.getTransactionsStatistics().getL_q(),3);
        updateL(simulation.getTransactionsStatistics().getL(),3);
        updateW_S(simulation.getTransactionsStatistics().getW_s(),3);
        updateW_Q(simulation.getTransactionsStatistics().getW_q(),3);
        updateW(simulation.getTransactionsStatistics().getW(),3);
        updateRho(simulation.getTransactionsStatistics().getRho(),3);
        updateLeisureTime(simulation.getTransactionsStatistics().getLeisureTime(),3);
        updateModuleTime(DDL,simulation.getTransactionsStatistics().getAverageTime(DDL),3);
        updateModuleTime(UPDATE,simulation.getTransactionsStatistics().getAverageTime(UPDATE),3);
        updateModuleTime(SELECT,simulation.getTransactionsStatistics().getAverageTime(SELECT),3);
        updateModuleTime(JOIN,simulation.getTransactionsStatistics().getAverageTime(JOIN),3);
        updateAverageServedQueries(simulation.getTransactionsStatistics().getAmountOfServedQueries(),3);
        //QUERIES EXECUTION MODULE
        updateLambda(simulation.getQueriesExecutionStatistics().getLambda(),4);
        updateMu(simulation.getQueriesExecutionStatistics().getMu(),4);
        updateL_S(simulation.getQueriesExecutionStatistics().getL_s(),4);
        updateL_Q(simulation.getQueriesExecutionStatistics().getL_q(),4);
        updateL(simulation.getQueriesExecutionStatistics().getL(),4);
        updateW_S(simulation.getQueriesExecutionStatistics().getW_s(),4);
        updateW_Q(simulation.getQueriesExecutionStatistics().getW_q(),4);
        updateW(simulation.getQueriesExecutionStatistics().getW(),4);
        updateRho(simulation.getQueriesExecutionStatistics().getRho(),4);
        updateLeisureTime(simulation.getQueriesExecutionStatistics().getLeisureTime(),4);
        updateModuleTime(DDL,simulation.getQueriesExecutionStatistics().getAverageTime(DDL),4);
        updateModuleTime(UPDATE,simulation.getQueriesExecutionStatistics().getAverageTime(UPDATE),4);
        updateModuleTime(SELECT,simulation.getQueriesExecutionStatistics().getAverageTime(SELECT),4);
        updateModuleTime(JOIN,simulation.getQueriesExecutionStatistics().getAverageTime(JOIN),4);
        updateAverageServedQueries(simulation.getQueriesExecutionStatistics().getAmountOfServedQueries(),4);
        // GENERAL QUERIES
        updateAverageRejectedQueries(simulation.getQueryStatistics().getNumberOfRejectedQueries());
        updateAverageKilledQueries(simulation.getQueryStatistics().getNumberKilledQueries());
        updateAverageLifespan(simulation.getQueryStatistics().getAvgLifespanOfQuery());
    }
}