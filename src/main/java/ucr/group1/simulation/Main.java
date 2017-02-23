package ucr.group1.simulation;

/**
 * Created by Daniel on 12/2/2017.
 */
public class Main {
    public static void main(String[]  args){
        for(int i = 1; i <= 15; i++){
            int timeOut = 7;
            Simulation simulation = new Simulation(20,8,10,2,
                    timeOut,false, 1, 15000);
            simulation.simulate();
            simulation.createATimeLogArchive("Bitacora" + i);
        }
    }
}