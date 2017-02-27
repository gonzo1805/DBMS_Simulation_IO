package ucr.group1.simulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ucr.group1.ui.Controller;

/**
 * Created by Daniel on 12/2/2017.
 */
public class Main extends Application {
    public static void main(String[]  args){
        launch(args);
        /*Simulation simulation = new Simulation(15, 3, 2,1
        ,15,false,0,15000, null);
        simulation.simulate();
        simulation.createATimeLogArchive("bitacora de prueba");
        System.out.println("Lambda de conexion: " + simulation.getClientManagementStatistics().getLambda());
        System.out.println("Mu de conexion: " + simulation.getClientManagementStatistics().getMu());
        System.out.println("tiempo promedio de vida de los querys: "+ simulation.getQueryStatistics().getAvgLifespanOfQuery());*/
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Simulation Group 1 Main Menu");
        primaryStage.setScene(new Scene(root, 804, 359));
        primaryStage.show();
    }
}