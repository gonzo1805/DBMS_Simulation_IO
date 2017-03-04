package ucr.group1.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import ucr.group1.event.Event;
import ucr.group1.html.htmlGenerator;
import ucr.group1.query.Query;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.SimulationsStatistics;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Time;
import java.util.*;

import static ucr.group1.event.EventType.A_NEW_QUERY_IS_REQUESTING;

/**
 * Created by Gonzalo and Daniel on 2/20/2017.
 */
public class Controller extends Application implements Initializable {

    /**
     * Parameters of the simulation
     */
    private int kConcurrentConection;
    private int nVerificationServers;
    private int pExecutionServers;
    private int mTransactionServers;
    private int tTimeout;
    private boolean slowMode;
    private int timeBetEvents;
    private int simulationTime;
    private int amountOfRuns;
    private Simulation simulation;
    private SimulationsStatistics simulationsStatistics;
    final ToggleGroup group = new ToggleGroup();

    // A list for the comboBox of the UI
    ObservableList<String> modules = FXCollections.observableArrayList("ClientManagementModule", "System Call", "QueriesVerificationModule",
            "TransactionsModule", "QueriesExecutionModule");

    /**
     * All the Text Fields of the UI
     */

    @FXML
    private TextField txtRuns;

    @FXML
    private TextField txtTimePerRun;

    @FXML
    private TextField txtTimeBetEvents;

    @FXML
    private TextField txtTimeout;

    @FXML
    private TextField txtKConnection;

    @FXML
    private TextField txtNProc;

    @FXML
    private TextField txtPTrans;

    @FXML
    private TextField txtMConsults;

    /**
     * All the Labels of the UI
     */

    @FXML
    private Label labelRuns;

    @FXML
    private Label labelTimePerRun;

    @FXML
    private Label labelTimeBetEvents;

    @FXML
    private Label labelTimeout;

    @FXML
    private Label labelKConnection;

    @FXML
    private Label labelNProc;

    @FXML
    private Label labelPTrans;

    @FXML
    private Label labelMConsults;

    @FXML
    private Label labelSimulationClock;

    @FXML
    private Label labelActualEvent;

    @FXML
    private Label labelRejectedConections;

    @FXML
    private Label labelamountOfServers;

    @FXML
    private Label labelbusyServers;

    @FXML
    private Label labelqueueLenght;

    @FXML
    private Label labelclientsServed;

    /**
     * The Combo Box of the UI
     */

    @FXML
    private ComboBox<String> comboBoxModule;

    /**
     * Radio Buttons of the UI
     */

    @FXML
    private RadioButton radioButtonYes;

    @FXML
    private RadioButton radioButtonNo;

    //////////////////////////////////Begin of click methods on textFields//////////////////////////////////////////////

    // Methods for the action of click on a txtField, erase all the text that the text field have on it
    @FXML
    void clickKConnection(MouseEvent event) {
        txtKConnection.setText("");
    }

    @FXML
    void clickMConsults(MouseEvent event) {
        txtMConsults.setText("");
    }

    @FXML
    void clickNProc(MouseEvent event) {
        txtNProc.setText("");
    }

    @FXML
    void clickPTrans(MouseEvent event) {
        txtPTrans.setText("");
    }

    @FXML
    void clickRuns(MouseEvent event) {
        txtRuns.setText("");
    }

    @FXML
    void clickTimeBetEvents(MouseEvent event) {
        txtTimeBetEvents.setText("");
    }

    @FXML
    void clickTimePerRun(MouseEvent event) {
        txtTimePerRun.setText("");
    }

    @FXML
    void clickTimeout(MouseEvent event) {
        txtTimeout.setText("");
    }

    ////////////////////////////////////End of click methods on textField///////////////////////////////////////////////

    ///////////////////////////////Begin of the press enter methods on textField////////////////////////////////////////

    // Methods for the action of press enter on the textField, put the label assigned to the textField on the value of
    // the textField, also it do data verification, it uses a simple regex to know if the inserted data is valid or not
    @FXML
    void enterKConnection(ActionEvent event) {
        if (!txtKConnection.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            kConcurrentConection = Integer.parseInt(txtKConnection.getText());
            labelKConnection.setText(txtKConnection.getText());
        }
    }

    @FXML
    void enterMConsults(ActionEvent event) {
        if (!txtMConsults.getText().matches("^[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            mTransactionServers = Integer.parseInt(txtMConsults.getText());
            labelMConsults.setText(txtMConsults.getText());
        }
    }

    @FXML
    void enterNProc(ActionEvent event) {
        if (!txtNProc.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            nVerificationServers = Integer.parseInt(txtNProc.getText());
            labelNProc.setText(txtNProc.getText());
        }
    }

    @FXML
    void enterPTrans(ActionEvent event) {
        if (!txtPTrans.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            pExecutionServers = Integer.parseInt(txtPTrans.getText());
            labelPTrans.setText(txtPTrans.getText());
        }
    }

    @FXML
    void enterRuns(ActionEvent event) {
        if (!txtRuns.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            amountOfRuns = Integer.parseInt(txtRuns.getText());
            labelRuns.setText(txtRuns.getText());
        }
    }

    @FXML
    void enterTimeBetEvents(ActionEvent event) {
        if (!txtTimeBetEvents.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            timeBetEvents = Integer.parseInt(txtTimeBetEvents.getText());
            labelTimeBetEvents.setText(txtTimeBetEvents.getText());
        }
    }

    @FXML
    void enterTimePerRun(ActionEvent event) {
        if (!txtTimePerRun.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            simulationTime = Integer.parseInt(txtTimePerRun.getText());
            labelTimePerRun.setText(txtTimePerRun.getText());
        }
    }

    @FXML
    void enterTimeout(ActionEvent event) {
        if (!txtTimeout.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            tTimeout = Integer.parseInt(txtTimeout.getText());
            labelTimeout.setText(txtTimeout.getText());
        }
    }

    //////////////////////////////////End of the press enter methods on textField///////////////////////////////////////

    /**
     * Completely starts the simulation with the parameters on the textFields, it also gather the stats from the
     * simulation and stacks it for .html, when the button is clicked, all the textField got blocked and when the
     * simulation finishes it all got unlocked
     *
     * @param event
     */

    @FXML
    void clickBotonStarSimulation(ActionEvent event) {
        // If you want the html with the stats
        int input = JOptionPane.showConfirmDialog(null, "¿Desea crear HTML para cada simulación?", "Html", JOptionPane.YES_NO_OPTION);
        // If yes
        if (input == 0) {
            JOptionPane.showMessageDialog(null,
                    "Los Html serán creados en la carpeta DBMS_Simulation_IO\\src\\main\\resources\\Statistics");
        }
        // A list of all the names of the simulations, for purposes of the .html
        List<String> simulationList = new LinkedList<>();
        // Blocks the textFields
        setAllTextAreasDisabled();
        // The stats for each simulation
        simulationsStatistics = new SimulationsStatistics();
        // If the user choose the slow mode on
        if (slowMode) {
            // The timeline that inserts the delay assigned by the user
            Timeline delay = new Timeline(new KeyFrame(Duration.seconds(timeBetEvents), new EventHandler<ActionEvent>() {
                // Counter
                int i = 0;

                // Set the labels and write on the textArea
                @Override
                public void handle(ActionEvent event) {
                    txtArea.appendText(toWrite.get(i).getLog());
                    labelActualEvent.setText(toWrite.get(i).getEvent());
                    labelSimulationClock.setText(String.valueOf(toWrite.get(i).getTime()));
                    i++;
                }
            }));
            // Work indefinite
            delay.setCycleCount(Timeline.INDEFINITE);
            // Start
            delay.play();
        }
        // For every run (the parameter)
        for (int i = 1; i <= amountOfRuns; i++) {
            // A new simulation
            simulation = new Simulation(kConcurrentConection, nVerificationServers, pExecutionServers,
                    mTransactionServers, tTimeout, slowMode, timeBetEvents, simulationTime, this);
            // The simulation ID
            int idAssigner = 1;
            Event firstEvent = new Event(A_NEW_QUERY_IS_REQUESTING, 0, new Query(idAssigner++, simulation.getGenerator()));
            // Add the first event
            simulation.addEvent(firstEvent);
            // Start it
            while (simulation.getTime() < simulation.getTimePerSimulation()) {
                simulation.processNextEvent(idAssigner);
            }
            // Get the stats of the simulation
            simulationsStatistics.addSimulation(simulation);
            // Create the personal html for this simulation
            htmlGenerator personalHtml = new htmlGenerator();
            // Fill the html with the general simulation parameters
            personalHtml.fillParameters(simulation, amountOfRuns, kConcurrentConection, pExecutionServers, mTransactionServers,
                    nVerificationServers, timeBetEvents, simulationTime, tTimeout, slowMode);
            // Create the html
            if (input == 0) {
                // Create the html of the personal simulations
                personalHtml.crea("simulation" + i, String.valueOf(i), simulation);
            }
            // Create the list for the links on the main
            simulationList.add("simulation" + i + ".html");

            //simulation.createATimeLogArchive("Bitacora" + i);
        }
        // Update the UI data display
        labelRejectedConections.setText(String.valueOf(simulationsStatistics.getAverageRejectedQueries()));
        // The index html
        htmlGenerator htmlGenerator = new htmlGenerator();
        htmlGenerator.fillParameters(simulation, amountOfRuns, kConcurrentConection, pExecutionServers, mTransactionServers,
                nVerificationServers, timeBetEvents, simulationTime, tTimeout, slowMode);
        // Create the index html
        if (input == 0) {
            htmlGenerator.createIndex(simulationList, simulationsStatistics);
        }
        setAllTextAreasEnabled();
        // It`s finished!
        if (!slowMode) {
            JOptionPane.showMessageDialog(null, "La simulación se ha completado", "Finalizada", 1);
        }
    }

    // This textArea and Linked List is for the constant update of the UI in slow mode
    @FXML
    private TextArea txtArea;

    private LinkedList<Printer> toWrite = new LinkedList();

    /**
     * Update the LinkedList with the Printers objects to show on screen in case of SlowMode == true
     *
     * @param toWrite
     */
    public void updateTextArea(Printer toWrite) {
        this.toWrite.add(toWrite);
    }

    /**
     * Set all the labels clean again and restart the simulation with the same values
     *
     * @param event
     */
    @FXML
    void clickRestart(ActionEvent event) {
        labelRejectedConections.setText("");
        labelActualEvent.setText("");
        labelSimulationClock.setText("");
        labelActualEvent.setText("");
        labelclientsServed.setText("");
        labelqueueLenght.setText("");
        labelamountOfServers.setText("");
        labelbusyServers.setText("");
        clickBotonStarSimulation(event);
    }

    /**
     * Set the default values and clean the labels
     * @param event
     */
    @FXML
    void clickNew(ActionEvent event) {
        labelRejectedConections.setText("");
        labelActualEvent.setText("");
        labelSimulationClock.setText("");
        labelActualEvent.setText("");
        labelclientsServed.setText("");
        labelqueueLenght.setText("");
        labelamountOfServers.setText("");
        labelbusyServers.setText("");
        clickDefault(event);

    }

    /**
     * Set the parameters by a default value, chosen by us
     * @param event
     */
    @FXML
    void clickDefault(ActionEvent event) {
        setAllLabels(15, 20, 10, 2, 8, 0, 15000, 15);
        amountOfRuns = 15;
        kConcurrentConection = 20;
        pExecutionServers = 10;
        mTransactionServers = 2;
        nVerificationServers = 8;
        timeBetEvents = 0;
        simulationTime = 15000;
        tTimeout = 15;
        radioButtonNo.setSelected(true);
        slowMode = false;
        txtTimeBetEvents.setDisable(true);
    }

    /**
     * Set all the labels with the parameters
     *
     * @param amountOfRuns         the amount of runs of the simulation
     * @param kConcurrentConection the k concurrent conections of the simulation
     * @param pExecutionServers    the p execution servers of the simulation
     * @param mTransactionServers  the m transaction servers of the simulation
     * @param nVerificationServers the n verification servers of the simulation
     * @param timeBetEvents        the time between events
     * @param simulationTime       the simulation time
     * @param tTimeout             the t timeout of the simulation
     */
    private void setAllLabels(int amountOfRuns, int kConcurrentConection, int pExecutionServers, int mTransactionServers,
                              int nVerificationServers, int timeBetEvents, int simulationTime,
                              int tTimeout) {
        labelTimeout.setText(String.valueOf(tTimeout));
        labelRuns.setText(String.valueOf(amountOfRuns));
        labelPTrans.setText(String.valueOf(pExecutionServers));
        labelKConnection.setText(String.valueOf(kConcurrentConection));
        labelMConsults.setText(String.valueOf(mTransactionServers));
        labelNProc.setText(String.valueOf(nVerificationServers));
        labelTimeBetEvents.setText(String.valueOf(timeBetEvents));
        labelTimePerRun.setText(String.valueOf(simulationTime));
    }

    /**
     * Block the textField of the timeBetEvents, because it is only used on the slowMode
     * @param event
     */
    @FXML
    void clickRadioButtonNo(ActionEvent event) {
        slowMode = false;
        txtTimeBetEvents.setDisable(true);
    }

    /**
     * Unblock the textField of the timeBetEvents, because it is only used on the slowMode
     * @param event
     */
    @FXML
    void clickRadioButtonYes(ActionEvent event) {
        slowMode = true;
        txtTimeBetEvents.setDisable(false);
    }

    /**
     * Initializer
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxModule.setValue("Modulo");
        comboBoxModule.setItems(modules);
        radioButtonNo.setToggleGroup(group);
        radioButtonYes.setToggleGroup(group);
        labelActualEvent.setText("");
        labelSimulationClock.setText("");
        labelActualEvent.setText("");
        labelclientsServed.setText("");
        labelqueueLenght.setText("");
        labelamountOfServers.setText("");
        labelbusyServers.setText("");
        labelRejectedConections.setText("");
    }

    /**
     * Disable all the textFields of the UI
     */
    private void setAllTextAreasDisabled() {
        txtKConnection.setDisable(true);
        txtPTrans.setDisable(true);
        txtTimeout.setDisable(true);
        txtMConsults.setDisable(true);
        txtNProc.setDisable(true);
        txtRuns.setDisable(true);
        txtTimeBetEvents.setDisable(true);
        txtTimePerRun.setDisable(true);
        radioButtonYes.setDisable(true);
        radioButtonNo.setDisable(true);
    }

    /**
     * Enable all the textFields of the UI
     */
    private void setAllTextAreasEnabled() {
        txtKConnection.setDisable(false);
        txtPTrans.setDisable(false);
        txtTimeout.setDisable(false);
        txtMConsults.setDisable(false);
        txtNProc.setDisable(false);
        txtRuns.setDisable(false);
        txtTimePerRun.setDisable(false);
        radioButtonYes.setDisable(false);
        radioButtonNo.setDisable(false);
    }

    /**
     * Creates and shows the UI
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Simulation Group 1 Main Menu");
        primaryStage.setScene(new Scene(root, 1063, 359));
        primaryStage.show();
        txtArea = new TextArea();
        // On close action shut down all te app
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }

    /**
     * Calls the start method
     * @param args
     */
    public void begin(String[] args) {
        launch(args);
    }

    /**
     * The method in charge of the display of stats on the comboBox, it shows the stats required by the project
     * statement
     * @param event
     */
    @FXML
    void chooseAnOption(ActionEvent event) {
        switch (comboBoxModule.getValue()) {
            case "ClientManagementModule":
                labelamountOfServers.setText(String.valueOf(kConcurrentConection));
                labelbusyServers.setText(String.valueOf(simulation.getNumberBusyServersOnClientManagementModule()));
                labelqueueLenght.setText(String.valueOf(simulationsStatistics.getL_q(0)));
                labelclientsServed.setText(String.valueOf(simulationsStatistics.getAmountOfServedQueries(0)));
                break;
            case "System Call":
                labelamountOfServers.setText(String.valueOf("1"));
                labelbusyServers.setText(String.valueOf(simulation.getNumberBusyServersOnProcessesManagementModule()));
                labelqueueLenght.setText(String.valueOf(simulationsStatistics.getL_q(1)));
                labelclientsServed.setText(String.valueOf(simulationsStatistics.getAmountOfServedQueries(1)));
                break;
            case "QueriesVerificationModule":
                labelamountOfServers.setText(String.valueOf(nVerificationServers));
                labelbusyServers.setText(String.valueOf(simulation.getNumberBusyServersOnQueriesVerificationModule()));
                labelqueueLenght.setText(String.valueOf(simulationsStatistics.getL_q(2)));
                labelclientsServed.setText(String.valueOf(simulationsStatistics.getAmountOfServedQueries(2)));
                break;
            case "TransactionsModule":
                labelamountOfServers.setText(String.valueOf(mTransactionServers));
                labelbusyServers.setText(String.valueOf(simulation.getNumberBusyServersOnTransactionsModule()));
                labelqueueLenght.setText(String.valueOf(simulationsStatistics.getL_q(3)));
                labelclientsServed.setText(String.valueOf(simulationsStatistics.getAmountOfServedQueries(3)));
                break;
            case "QueriesExecutionModule":
                labelamountOfServers.setText(String.valueOf(pExecutionServers));
                labelbusyServers.setText(String.valueOf(simulation.getNumberBusyServersOnQueriesExecutionModule()));
                labelqueueLenght.setText(String.valueOf(simulationsStatistics.getL_q(4)));
                labelclientsServed.setText(String.valueOf(simulationsStatistics.getAmountOfServedQueries(4)));
                break;
            default:
                break;
        }
    }
}
