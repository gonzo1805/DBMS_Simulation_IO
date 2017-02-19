package ucr.group1.statistics;

import ucr.group1.module.Module;
import ucr.group1.query.Query;
import ucr.group1.query.QueryType;
import ucr.group1.simulation.Simulation;

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

    public ModuleStatistics(Module module, Simulation simulation){
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
    }

    public double getLambda(){
        return  (1/timeBetweenArrives);
    }

    public double getMu(){
        return (1/getW_s());
    }

    public double getRho(){
        return (getW_s()/(module.getNumberOfServers()*timeBetweenArrives));
    }

    public double getL(){
        return (getL_q() + getL_s());
    }

    public double getL_s(){
        return l_s;
    }

    public double getL_q(){
        return l_q;
    }

    public double getW(){
        return (getL()*timeBetweenArrives);
    }

    public double getW_s(){
        return (getL_s()*timeBetweenArrives);
    }

    public double getW_q(){
        return (getL_q()*timeBetweenArrives);
    }

    public double getAverageTime(QueryType type){
        switch(type.getType()){
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

    public double getLeisureTime(){
        double rho = getRho();
        if(rho < 1) {
            return ((1 - rho) * simulation.getTime());
        }
        else {
            return 0;
        }
    }

    public void updateTimeBetweenArrives(double newTimeBetweenArrives){
        numberTimeBetweenArrives++;
        double percentage = (numberTimeBetweenArrives-1)/(numberTimeBetweenArrives);
        timeBetweenArrives *= percentage;
        timeBetweenArrives += (1 - percentage)*(newTimeBetweenArrives-lastArrive);
        lastArrive = newTimeBetweenArrives;
    }

    public void updateL_Q(int newL_q){
        numberL_Q++;
        double percentage = (numberL_Q-1)/(numberL_Q);
        l_q *= percentage;
        l_q += (1 - percentage)*newL_q;
    }

    public void updateL_S(int newL_s){
        numberL_S++;
        double percentage = (numberL_S-1)/(numberL_S);
        l_s *= percentage;
        l_s += (1 - percentage)*newL_s;
    }

    public void updateModuleTime(Query query, double newModuleTime){
        double percentage;
        switch (query.getType()) {
            case DDL:
                numberDdlMT++;
                percentage = (numberDdlMT-1)/(numberDdlMT);
                ddlMT *= percentage;
                ddlMT += (1 - percentage)*newModuleTime;
                break;
            case UPDATE:
                numberUpdateMT++;
                percentage = (numberUpdateMT-1)/(numberUpdateMT);
                updateMT *= percentage;
                updateMT += (1 - percentage)*newModuleTime;
                break;
            case SELECT:
                numberSelectMT++;
                percentage = (numberSelectMT-1)/(numberSelectMT);
                selectMT *= percentage;
                selectMT += (1 - percentage)*newModuleTime;
                break;
            case JOIN:
                numberJoinMT++;
                percentage = (numberJoinMT-1)/(numberJoinMT);
                joinMT *= percentage;
                joinMT += (1 - percentage)*newModuleTime;
                break;
        }
    }
}
