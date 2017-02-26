package ucr.group1.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ucr.group1.html.htmlGenerator;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.SimulationsStatistics;

import javax.swing.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    /**
     * Parameters of the simulation
     */
    int kConcurrentConection;
    int nVerificationServers;
    int pExecutionServers;
    int mTransactionServers;
    int tTimeout;
    boolean slowMode;
    int timeBetEvents;
    int simulationTime;
    int amountOfRuns;
    Simulation simulation;

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

    @FXML
    private TextArea mainTextArea;

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


    /**
     * The Combo Box of the UI
     */

    @FXML
    private ComboBox<String> comboBoxModule;

    /**
     * Buttons and Radio Buttons of the UI
     */

    @FXML
    private Button botonStartSimulation;

    @FXML
    private Button buttonRestartSimulation;

    @FXML
    private Button buttonNewSimulation;

    @FXML
    private Button buttonDefault;

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
        // A list of all the names of the simulations, for purposes of the .html
        List<String> simulationList = new LinkedList<>();
        // Blocks the textFields
        setAllTextAreasDisabled();
        // The stats for each simulation
        SimulationsStatistics simulationsStatistics = new SimulationsStatistics();
        // For every run (the parameter)
        for (int i = 1; i <= amountOfRuns; i++) {
            // A new simulation
            simulation = new Simulation(kConcurrentConection, nVerificationServers, pExecutionServers,
                    mTransactionServers, tTimeout, slowMode, timeBetEvents, simulationTime);
            // Start it
            simulation.simulate();
            // Get the stats of the simulation
            simulationsStatistics.addSimulation(simulation);
            // Create the personal html for this simulation
            htmlGenerator personalHtml = new htmlGenerator();
            // Fill the html with the general simulation parameters
            personalHtml.fillParameters(simulation, amountOfRuns, kConcurrentConection, pExecutionServers, mTransactionServers,
                    nVerificationServers, timeBetEvents, simulationTime, tTimeout, slowMode);
            // Create the html
            personalHtml.crea("simulation" + i, String.valueOf(i), simulation);
            // Create the list for the links on the main
            simulationList.add("simulation" + i + ".html");

            simulation.createATimeLogArchive("Bitacora" + i);
        }
        // The index html
        htmlGenerator htmlGenerator = new htmlGenerator();
        htmlGenerator.fillParameters(simulation, amountOfRuns, kConcurrentConection, pExecutionServers, mTransactionServers,
                nVerificationServers, timeBetEvents, simulationTime, tTimeout, slowMode);
        // Create the index html
        htmlGenerator.createIndex(simulationList, simulationsStatistics);
        setAllTextAreasEnabled();
        // It`s finished
        JOptionPane.showMessageDialog(null, "La simulación se ha completado", "Finalizada", 1);
    }

    private void updateTextArea(String toWrite) {
        mainTextArea.setText((mainTextArea.getText() + toWrite));
    }

    @FXML
    void clickRestart(ActionEvent event) {

    }

    @FXML
    void clickNew(ActionEvent event) {

    }

    /**
     * Set the parameters by a default value, chosen by us
     *
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
        slowMode = false;
        radioButtonNo.isSelected();
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
        final ToggleGroup group = new ToggleGroup();
        radioButtonNo.setToggleGroup(group);
        radioButtonYes.setToggleGroup(group);

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
        txtTimeBetEvents.setDisable(false);
        txtTimePerRun.setDisable(false);
        radioButtonYes.setDisable(false);
        radioButtonNo.setDisable(false);
    }

    public void setLabelRejectedConections(String toWrite) {
        labelRejectedConections.setText(toWrite);
    }

    public void setLabelSimulationClock(String toWrite) {
        labelSimulationClock.setText(toWrite);
    }

    public void setLabelActualEvent(String toWrite) {
        labelActualEvent.setText(toWrite);
    }
}
