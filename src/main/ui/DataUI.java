package ui;

import model.Column;
import model.Data;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


//Represents a UI application of Data Manipulator
//reference: CPSC 210 AlarmSystem https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
public class DataUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Data data;
    private JComboBox<String> printCombo;
    private JDesktopPane desktop;
    private JInternalFrame controlPanel;
    private List<Column> columnAdded;

    /**
     * Constructor sets up button panel.
     */
    public DataUI() {
        data = new Data();
        JsonReader reader = new JsonReader("./data/testReaderGeneralData.json");
        try {
            data = reader.read();
        } catch (IOException e) {
            //
        }
        columnAdded = new LinkedList<>();
        desktop = new JDesktopPane();
        //desktop.addMouseListener(new DesktopFocusAction());
        controlPanel = new JInternalFrame("Control Panel", false, false, false, false);
        controlPanel.setLayout(new BorderLayout());

        setContentPane(desktop);
        setTitle("Sunny's Data Manipulator");
        setSize(WIDTH, HEIGHT);

        addButtonPanel();
        //addMenu(); save and load .json

        controlPanel.pack();
        controlPanel.setVisible(true);
        desktop.add(controlPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);
    }

    /**
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1));
        buttonPanel.add(new JButton(new ColumnAction())); //add new column
        buttonPanel.add(new JButton(new PrintDataAction())); //view ALL or only ADDED
        buttonPanel.add(createPrintCombo());
        //buttonPanel.add(new JButton(new SpecifyTypeAction())); //specify data type

        controlPanel.add(buttonPanel, BorderLayout.WEST);
    }

    private class ColumnAction extends AbstractAction {
        ColumnAction() {
            super("Add Column");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            Column newCol = new Column();
            String name = JOptionPane.showInputDialog(null,
                    "Column Name?", "Enter Name of new Column", JOptionPane.QUESTION_MESSAGE);

            if (name != null) {
                newCol.renameCol(name);
                for (int i = 0; i < data.getNumOfRow(); i++) {
                    String input = JOptionPane.showInputDialog(null,
                            "Entry" + i, "Enter Entry" + i, JOptionPane.QUESTION_MESSAGE);
                    newCol.addElement(input);
                }
                data.addCol(newCol);
                columnAdded.add(newCol);
            }
        }
    }

    /**
     * Represents the action to be taken when the user wants to
     * print the Data.
     */
    private class PrintDataAction extends AbstractAction {
        PrintDataAction() {
            super("Print log to...");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String selected = (String) printCombo.getSelectedItem();
            ScreenPrinter sp;
            sp = new ScreenPrinter(DataUI.this);
            desktop.add((ScreenPrinter) sp);

            if (selected.equals("Whole Data")) {
                sp.printData(data);
            } else if (selected.equals("Newly Added Columns")) {
                sp.printColumns(columnAdded);
            }
        }

    }

    /**
     * Helper to create print options combo box
     * @return  the combo box
     */
    private JComboBox<String> createPrintCombo() {
        printCombo = new JComboBox<String>();
        printCombo.addItem("Whole Data");
        printCombo.addItem("Newly Added Columns");
        return printCombo;
    }

/*
    //EFFECT: if remove == True: removes column and return a new Column
    //          else return a chosen Column
    private Column chooseCol(Boolean remove) {
        String selection = "";
        while (!selection.equals("i") && !selection.equals("n")) {
            System.out.println("Choose Column by:");
            System.out.println("i -> integer index");
            System.out.println("n -> name of Column");
            selection = input.next();
        }

        if (selection.equals("i")) {
            return chooseColByInt(remove);
        } else {
            return chooseColByName(remove);
        }
    }

    //EFFECT: if remove == True: removes column and return a new Column
    //          else return a chosen Column
    private Column chooseColByInt(Boolean remove) {
        int n = -1;
        while (n < 0) {
            n = checkIntInput(data.getNumOfCol(), "integer index of col");
        }
        if (remove) {
            data.removeCol(n);
            return new Column();
        } else {
            return data.getCol(n);
        }
    }

    //EFFECT: if remove == True: removes column and return a new Column
    //          else return a chosen Column
    private Column chooseColByName(Boolean remove) {
        String s = "";
        while (!data.getNames().contains(s)) {
            System.out.print("Enter name of Column: ");
            s = input.next();
            if (!data.getNames().contains(s)) {
                System.out.println("Invalid name.");
            }
        }
        if (remove) {
            data.removeCol(s);
            return new Column();
        } else {
            return data.getCol(s);
        }
    }
*/

}
