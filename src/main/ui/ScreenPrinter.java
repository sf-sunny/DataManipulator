package ui;

import model.Column;
import model.Data;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Represents a screen printer for printing Data/Column(s) to screen.
 */
//reference: CPSC 210 AlarmSystem https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
public class ScreenPrinter extends JInternalFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private JTextArea textArea;

    /**
     * Constructor sets up window in which columns will be printed on screen
     *
     * @param parent the parent component
     */
    public ScreenPrinter(Component parent) {
        super("Columns", false, true, false, false);
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);
        setSize(WIDTH, HEIGHT);
        setPosition(parent);
        setVisible(true);
    }

    public void printData(Data data) {
        //printDataInfo();
        printColumns(data.getData());
    }

    public void printColumns(List<Column> list) {
        for (int i = -1; i < list.get(0).getSize(); i++) {
            String sep = "";
            for (Column col : list) {
                if (i == -1) {
                    textArea.setText(textArea.getText() + sep + col.getName());
                } else {
                    textArea.setText(textArea.getText() + sep + col.get(i));
                }
                sep = ",";
            }
            textArea.setText(textArea.getText() + "\n");
        }
        repaint();
    }
    /**
     * Sets the position of window in which log will be printed relative to
     * parent
     *
     * @param parent the parent component
     */
    private void setPosition(Component parent) {
        setLocation(parent.getWidth() - getWidth() - 20,
                parent.getHeight() - getHeight() - 20);
    }
}
