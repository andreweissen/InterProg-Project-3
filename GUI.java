/**
 * GUI.java - Constructs the GUI
 * Begun 07/23/17
 * @author Andrew Eissen
 */

package projectthree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Central class that handles the GUI, extends JFrame
 * @see javax.swing.JFrame
 */
public class GUI extends JFrame {

    // Window-related variables
    private String title;
    private int width;
    private int height;

    // GUI fields
    private JFrame frame;
    private JPanel mainPanel;
    private ButtonGroup buttonsGroup;
    private JRadioButton iterativeButton, recursiveButton;
    private JButton computeButton;
    private JTextField nField, resultField, efficiencyField;
    private JLabel nLabel, resultLabel, efficiencyLabel, iterativeLabel, recursiveLabel,
        computeLabel;

    /**
     * Fully parameterized constructor
     * @param title
     * @param width
     * @param height
     */
    public GUI(String title, int width, int height) {
        super(title);
        this.setWindowTitle(title);
        this.setWindowWidth(width);
        this.setWindowHeight(height);
    }

    /**
     * Default constructor
     */
    public GUI() {
        super("Project 3");
        this.setWindowTitle("Project 3");
        this.setWindowWidth(330);
        this.setWindowHeight(200);
    }

    /**
     * Setter for String title
     * @param title
     * @return void
     */
    private void setWindowTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for int width
     * @param width
     * @return void
     */
    private void setWindowWidth(int width) {
        if (width > 330) {
            this.width = width;
        } else {
            this.width = 330;
        }
    }

    /**
     * Setter for int height
     * @param height
     * @return void
     */
    private void setWindowHeight(int height) {
        if (height > 200) {
            this.height = height;
        } else {
            this.height = 200;
        }
    }

    /**
     * Method for constructing the GUI, called from main
     * @return void
     */
    private void constructGUI() {
        // Define JPanel
        mainPanel = new JPanel(new GridLayout(6, 2, 3, 10));

        // Assorted Labels
        nLabel = new JLabel("Enter n:");
        resultLabel = new JLabel("Result:");
        efficiencyLabel = new JLabel("Efficiency:");

        // Space-filling labels
        iterativeLabel = new JLabel("");
        recursiveLabel = new JLabel("");
        computeLabel = new JLabel("");

        // Radio buttons
        buttonsGroup = new ButtonGroup();
        iterativeButton = new JRadioButton("Iterative");
        recursiveButton = new JRadioButton("Recursive");
        iterativeButton.setSelected(true);

        buttonsGroup.add(iterativeButton);
        buttonsGroup.add(recursiveButton);

        // JTextFields
        nField = new JTextField();
        resultField = new JTextField();
        efficiencyField = new JTextField();

        resultField.setEditable(false);
        efficiencyField.setEditable(false);

        // Compute Button
        computeButton = new JButton("Compute");
        computeButton.addMouseListener(new GUIMouseAdapter());

        /*
         * Author's Notes
         * <br>
         * To create a design that closely models that of the design rubric, the author employed a
         * standard GridLayout pattern of 6x2, wherein each element (radio button, button, text
         * field) would have its own JLabel, regardless of whether or not that label would contain
         * any actual text content. In half of the cases, the only content was an empty string.
         */

        // Assemble GUI
        mainPanel.add(iterativeLabel);
        mainPanel.add(iterativeButton);
        mainPanel.add(recursiveLabel);
        mainPanel.add(recursiveButton);
        mainPanel.add(nLabel);
        mainPanel.add(nField);
        mainPanel.add(computeLabel);
        mainPanel.add(computeButton);
        mainPanel.add(resultLabel);
        mainPanel.add(resultField);
        mainPanel.add(efficiencyLabel);
        mainPanel.add(efficiencyField);

        // JFrame
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setContentPane(mainPanel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(new GUIWindowAdapter());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Main method
     * @param args
     * @return void
     */
    public static void main(String[] args) {
        GUI newGUI = new GUI();
        newGUI.constructGUI();
    }

    /**
     * Class for handling Compute button clicks, extends MouseAdapter
     * @see java.awt.event.MouseAdapter
     */
    class GUIMouseAdapter extends MouseAdapter {
        public GUIMouseAdapter() {}

        /**
         * Overrides MouseAdapter mousePressed method
         * @param e
         * return void
         */
        @Override
        public void mousePressed(MouseEvent e) {
            int result, userInput;

            try {
                userInput = Integer.parseInt(nField.getText());
            } catch (NumberFormatException error) {
                this.displayWarning("Improper input detected. Please try again.");
                return;
            }

            // Reset the value of the efficiency counter
            Sequence.resetEfficiency();

            /*
             * Author's Notes
             * <br>
             * Input restrictions were introduced to avoid excessively deep recursion or overflow in
             * cases wherein the computeRecursive function is invoked too many times. Max values of
             * 500 for the computeIterative function and 30 for the computeRecursive functions
             * seemed reasonable.
             */
            if (iterativeButton.isSelected() && userInput <= 500) {
                result = Sequence.computeIterative(userInput);
            } else if (recursiveButton.isSelected() && userInput <= 30) {
                result = Sequence.computeRecursive(userInput);
            } else {
                result = 0;
                this.displayWarning("Values of this size could result in errors.");
            }

            // Display in the uneditable textfields
            resultField.setText(Integer.toString(result));
            efficiencyField.setText(Integer.toString(Sequence.getEfficiency()));
        }

        /**
         * Pseudoconstructor; displays a JOptionPane window for warnings
         * @param message
         * @return void
         */
        private void displayWarning(String message) {
            nField.setText("");
            JOptionPane.showMessageDialog(
                mainPanel,
                "Error: " + message,
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Class for window close events, extends WindowAdapter
     * @see java.awt.event.WindowAdapter
     */
    class GUIWindowAdapter extends WindowAdapter {

        private String fileName;
        private BufferedWriter writer = null;

        /**
         * Parameterized constructor
         * @param fileName
         */
        public GUIWindowAdapter(String fileName) {
            this.fileName = fileName;
        }

        /**
         * Default constructor
         */
        public GUIWindowAdapter() {
            this.fileName = "results.csv";
        }

        /**
         * Overrides WindowAdapter windowClosing method
         * @param e
         * @return void
         */
        @Override
        public void windowClosing(WindowEvent e) {
            Sequence.resetEfficiency();
            this.calculateEfficiency();
        }

        /**
         * Calculates the efficiency of n values from 0 to 10 and prints to file
         * @return void
         */
        private void calculateEfficiency() {
            String iterativeEfficiency, recursiveEfficiency, completeLine;
            int valueN;

            try {
                writer = new BufferedWriter(new FileWriter(fileName));
                for (int n = 0; n <= 10; n++) {
                    valueN = Sequence.computeIterative(n);
                    iterativeEfficiency = Integer.toString(Sequence.getEfficiency());
                    Sequence.resetEfficiency();

                    valueN = Sequence.computeRecursive(n);
                    recursiveEfficiency = Integer.toString(Sequence.getEfficiency());
                    Sequence.resetEfficiency();

                    completeLine = Integer.toString(n) + "," + iterativeEfficiency + ","
                        + recursiveEfficiency;

                    if (n != 10) {
                        completeLine = completeLine.concat("\n");
                    }

                    writer.write(completeLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}