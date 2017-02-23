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

        /*for (int i = 1; i <= 15; i++) {
            int timeOut = 7;
            Simulation simulation = new Simulation(20, 8, 10, 2,
                    timeOut, false, 1, 15000);
            simulation.simulate();
            simulation.createATimeLogArchive("Bitacora" + i);
        }*/
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Simulation Group 1 Main Menu");
        primaryStage.setScene(new Scene(root, 804, 359));
        primaryStage.show();
    }
}