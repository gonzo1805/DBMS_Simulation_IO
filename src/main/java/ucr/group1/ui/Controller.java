package ucr.group1.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;

public class Controller {
    @FXML
    private Label labelMConsults;

    @FXML
    private TextField txtRuns;

    @FXML
    private TextField txtTimePerRun;

    @FXML
    private TextField txtSlowMode;

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
    private Label labelSlowMode;

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
    void clickSlowMode(MouseEvent event) {
        txtSlowMode.setText("");
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
    void enterSlowMode(ActionEvent event) {
        if (!txtSlowMode.getText().toLowerCase().equals("si") && !txtSlowMode.getText().toLowerCase().equals("no")) {
            JOptionPane.showMessageDialog(null, "Debe insertar Si o No nada más", "Error de inserción",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            if (txtSlowMode.getText().toLowerCase().equals("si")) {
                labelSlowMode.setText("Si");
            } else {
                labelSlowMode.setText("No");
            }

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
}
