package ucr.group1.ui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Gonzalo on 2/17/2017.
 */
public class Interface {
    private JPanel mainPanel;
    private JTextField runs;
    private JTextField timeBetEvents;
    private JTextField tTimeout;
    private JTextField runtime;
    private JTextField mProc;
    private JTextField kCon;
    private JTextField slowMode;
    private JTextField nProc;
    private JTextField pProc;
    private JButton buttomCorridas;
    private JButton buttonTiempoCorr;
    private JButton buttonSlowMode;
    private JButton buttonKCon;
    private JButton buttonNProc;
    private JButton buttonPProc;
    private JButton buttonMProc;
    private JButton buttonTTimeout;
    private JButton buttonTimeBetEvents;
    private JLabel defRuns;
    private JLabel defRuntime;
    private JLabel defSlowMode;
    private JLabel defKCon;
    private JLabel defNProc;
    private JLabel defPProc;
    private JLabel defMProc;
    private JLabel defTTimeout;
    private JLabel defTimeBetEvents;

    public Interface() {


        runs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runs.setText("");
            }
        });
        buttomCorridas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                defRuns.setText(runs.getText());

            }
        });
        runtime.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runtime.setText("");
            }
        });

        kCon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                kCon.setText("");
            }
        });
        nProc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                nProc.setText("");
            }
        });
        pProc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                pProc.setText("");
            }
        });
        mProc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mProc.setText("");
            }
        });
        tTimeout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tTimeout.setText("");
            }
        });
        timeBetEvents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                timeBetEvents.setText("");
            }
        });
        slowMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                slowMode.setText("");
            }
        });
        buttonTiempoCorr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                defRuntime.setText(runtime.getText());
            }
        });
        buttonSlowMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                defSlowMode.setText(slowMode.getText());
            }
        });
        buttonKCon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                defKCon.setText(kCon.getText());
            }
        });
        buttonNProc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                defNProc.setText(nProc.getText());
            }
        });
        buttonPProc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                defPProc.setText(pProc.getText());
            }
        });
        buttonMProc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                defMProc.setText(mProc.getText());
            }
        });
        buttonTTimeout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                defTTimeout.setText(tTimeout.getText());
            }
        });
        buttonTimeBetEvents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                defTimeBetEvents.setText(timeBetEvents.getText());
            }
        });
    }

    public static void main(String[] args) {
        // Name of the window
        JFrame mainFrame = new JFrame("Simulation");
        // Assignation of the main panel to the frame
        mainFrame.setContentPane(new Interface().mainPanel);
        // Set the close
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Compact the frame
        mainFrame.pack();
        // Set the location
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
