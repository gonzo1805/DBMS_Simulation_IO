package ucr.group1.ui;

import javax.swing.*;

/**
 * Created by Gonzalo on 2/17/2017.
 */
public class RunInterface {
    private JPanel panel;
    private JTextArea aquiIraLaBitacoraTextArea;


    public static void main(String[] args) {
        // Name of the window
        JFrame mainFrame = new JFrame("Simulation");
        // Assignation of the main panel to the frame
        mainFrame.setContentPane((new RunInterface().panel));
        // Set the close
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Compact the frame
        mainFrame.pack();
        // Set the location
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
