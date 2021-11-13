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
        super("Columns", true, true, false, false);
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);
        setSize(WIDTH, HEIGHT);
        setPosition(parent);
        setVisible(true);
    }

    public void printData(Data data) {
        printDataInfo(data);
        printColumns(data.getData());
    }

    public void printDataInfo(Data data) {
        String indexColInfo = "";
        if (data.getIndex() == -1) {
            indexColInfo = "\nIndex Column: None";
        } else {
            indexColInfo = "\nIndex: " + data.getCol(data.getIndex()).getName()
                    + " with Column Number: " + data.getIndex();
        }
        textArea.setText(textArea.getText() + indexColInfo
                + "\nColumn Name, Data Type (i: int, d: double, s: string, o: object (default)\n");
        for (int i = 0; i < data.getNumOfCol(); i++) {
            textArea.setText(textArea.getText() + data.getCol(i).getName() + "," + data.getCol(i).getType() + "\n");
        }
        textArea.setText(textArea.getText() + "Data Total Size (row x col): " + data.getNumOfRow()
                + " x " + data.getNumOfCol() + "\n\n");

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
