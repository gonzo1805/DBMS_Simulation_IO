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

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Simulation Group 1 Main Menu");
        primaryStage.setScene(new Scene(root, 804, 359));
        primaryStage.show();
    }
}