package ucr.group1.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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
            labelKConnection.setText(txtKConnection.getText());
        }
    }

    @FXML
    void enterMConsults(ActionEvent event) {
        if (!txtMConsults.getText().matches("^[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            labelMConsults.setText(txtMConsults.getText());
        }
    }

    @FXML
    void enterNProc(ActionEvent event) {
        if (!txtNProc.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            labelNProc.setText(txtNProc.getText());
        }
    }

    @FXML
    void enterPTrans(ActionEvent event) {
        if (!txtPTrans.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            labelPTrans.setText(txtPTrans.getText());
        }
    }

    @FXML
    void enterRuns(ActionEvent event) {
        if (!txtRuns.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            labelRuns.setText(txtRuns.getText());
        }
    }

    @FXML
    void enterTimeBetEvents(ActionEvent event) {
        if (!txtTimeBetEvents.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            labelTimeBetEvents.setText(txtTimeBetEvents.getText());
        }
    }

    @FXML
    void enterTimePerRun(ActionEvent event) {
        if (!txtTimePerRun.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            labelTimePerRun.setText(txtTimePerRun.getText());
        }
    }

    @FXML
    void enterTimeout(ActionEvent event) {
        if (!txtTimeout.getText().matches("[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Debe insertar numeros nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            labelTimeout.setText(txtTimeout.getText());
        }
    }

    @FXML
    void clickBotonStarSimulation(ActionEvent event) {
        setAllTextAreasDisabled();
        buttonDefault.setText("Cancelar");
    }


    @FXML
    void clickRestart(ActionEvent event) {

    }

    @FXML
    void clickNew(ActionEvent event) {

    }

    @FXML
    void clickDefault(ActionEvent event) {
        if (buttonDefault.getText().equals("Cancelar")) {
            setAllTextAreasEnabled();
            buttonDefault.setText("Default");
        } else {

        }
    }

    @FXML
    void clickRadioButtonNo(ActionEvent event) {
        txtTimeBetEvents.setDisable(true);
    }

    @FXML
    void clickRadioButtonYes(ActionEvent event) {
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
