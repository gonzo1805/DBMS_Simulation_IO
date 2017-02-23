package ucr.group1.simulation;

import ucr.group1.event.Event;
import ucr.group1.module.Connection;
import ucr.group1.query.Query;
import ucr.group1.query.QueryType;

import java.util.Queue;

/**
 * Created by Daniel on 12/2/2017.
 */
public class Main {
    public static void main(String[]  args){
        int timeOut = 3;
        Simulation simulation = new Simulation(20,8,10,2,
                timeOut,false, 0);
        simulation.simulate();
        simulation.createATimeLogArchive("Bitacora2");
    }
}