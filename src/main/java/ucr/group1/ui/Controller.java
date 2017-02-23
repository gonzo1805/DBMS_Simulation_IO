package ucr.group1.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ucr.group1.simulation.Simulation;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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

    ObservableList<String> modules = FXCollections.observableArrayList("ClientManagementModule", "System Call", "QueriesVerificationModule",
            "TransactionsModule", "QueriesExecutionModule");

    @FXML
    private Label labelMConsults;

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
    private ComboBox<String> comboBoxModule;

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

    @FXML
    void clickBotonStarSimulation(ActionEvent event) {
        setAllTextAreasDisabled();
        for (int i = 1; i <= 15; i++) {
            simulation = new Simulation(kConcurrentConection, nVerificationServers, pExecutionServers,
                    mTransactionServers, tTimeout, slowMode, timeBetEvents, simulationTime);
            simulation.simulate();
            simulation.createATimeLogArchive("Bitacora" + i);
        }
        setAllTextAreasEnabled();
    }


    @FXML
    void clickRestart(ActionEvent event) {

    }

    @FXML
    void clickNew(ActionEvent event) {

    }

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

    @FXML
    void clickRadioButtonNo(ActionEvent event) {
        slowMode = false;
        txtTimeBetEvents.setDisable(true);
    }

    @FXML
    void clickRadioButtonYes(ActionEvent event) {
        slowMode = true;
        txtTimeBetEvents.setDisable(false);
    }


    public void initialize(URL location, ResourceBundle resources) {
        comboBoxModule.setValue("Modulo");
        comboBoxModule.setItems(modules);
        final ToggleGroup group = new ToggleGroup();
        radioButtonNo.setToggleGroup(group);
        radioButtonYes.setToggleGroup(group);

    }

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
}
