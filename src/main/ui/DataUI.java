package ui;

import model.Column;
import model.Data;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private List<Column> columnAdded; // to be changed as a part of Data...

    /**
     * Constructor sets up button panel.
     */
    public DataUI() throws InterruptedException {
        showLogo();
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
        addMenu(); //save and load .json

        controlPanel.pack();
        controlPanel.setVisible(true);
        desktop.add(controlPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);
    }

    private void showLogo() throws InterruptedException {
        JFrame logoFrame = new JFrame();
        logoFrame.setLayout(new BorderLayout());
        logoFrame.setSize(300,250);
        logoFrame.setAlwaysOnTop(true);
        logoFrame.setUndecorated(true);
        JPanel logoPanel = new JPanel();
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(new ImageIcon("./data/IMG_0017.jpg").getImage().getScaledInstance(
                200, 200, Image.SCALE_DEFAULT)));
        logoLabel.setText("Welcome to Sunny's Data Manipulator");
        logoLabel.setHorizontalTextPosition(JLabel.CENTER);
        logoLabel.setVerticalTextPosition(JLabel.BOTTOM);
        logoPanel.add(logoLabel);
        Timer timer = new Timer(5000, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                logoFrame.dispose();
            }
        });
        logoFrame.add(logoPanel);
        centreOnScreen(logoFrame);
        logoFrame.setVisible(true);
        timer.start();
        java.util.concurrent.TimeUnit.SECONDS.sleep(4);
    }

    /**
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    private void centreOnScreen(JFrame frame) {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation((width - frame.getWidth()) / 2, (height - frame.getHeight()) / 2);
    }


    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1));
        buttonPanel.add(new JButton(new ColumnAction())); //add new column
        buttonPanel.add(new JButton(new SpecifyTypeAction())); //specify data type
        buttonPanel.add(new JButton(new PrintDataAction())); //view ALL or only ADDED
        buttonPanel.add(createPrintCombo());


        controlPanel.add(buttonPanel, BorderLayout.WEST);
    }

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        addMenuItem(fileMenu, new SaveDataAction(),
                KeyStroke.getKeyStroke("control S"));
        addMenuItem(fileMenu, new LoadDataAction(),
                KeyStroke.getKeyStroke("control O"));
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Adds an item with given handler to the given menu
     * @param theMenu  menu to which new item is added
     * @param action   handler for new menu item
     * @param accelerator    keystroke accelerator for this menu item
     */
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    private class SaveDataAction extends AbstractAction {
        SaveDataAction() {
            super("Save Data As .json");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JTextField fileNameBox = new JTextField();
            String[] fileTypesChoices = {".json"};
            JComboBox fileTypeBox = new JComboBox(fileTypesChoices);
            JPanel loadPanel = newDataPanel(fileNameBox, fileTypeBox);

            int result = JOptionPane.showConfirmDialog(null, loadPanel,
                    "Enter the file name under ./data/ to be imported: ", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String fileName = fileNameBox.getText() + fileTypesChoices[fileTypeBox.getSelectedIndex()];
                boolean performed = fileTypeBox.getSelectedIndex() == 0 ? saveJson(fileName) : false;
                if (performed) {
                    JOptionPane.showMessageDialog(null, fileName + " has been saved successfully.",
                            "Successfully Saved.", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,"Save .json file Unsuccessful. "
                            + "Please check if there is any typo.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private boolean saveJson(String fileName) {
            try {
                JsonWriter writer = new JsonWriter("./data/" + fileName);
                writer.open();
                writer.write(data);
                writer.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    private JPanel newDataPanel(JTextField fileNameBox, JComboBox fileTypeBox) {
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(2,2));
        dataPanel.add(new JLabel("file name:"));
        dataPanel.add(new JLabel());
        dataPanel.add(fileNameBox);
        dataPanel.add(fileTypeBox);
        return dataPanel;
    }

    private class LoadDataAction extends AbstractAction {
        LoadDataAction() {
            super("Load Data As..");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JTextField fileNameBox = new JTextField();
            String[] fileTypesChoices = {".json",".csv"};
            JComboBox fileTypeBox = new JComboBox(fileTypesChoices);
            JPanel loadPanel = newDataPanel(fileNameBox, fileTypeBox);

            int result = JOptionPane.showConfirmDialog(null, loadPanel,
                    "Enter the file name under ./data/ to be imported: ", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String fileName = fileNameBox.getText() + fileTypesChoices[fileTypeBox.getSelectedIndex()];
                boolean performed = fileTypeBox.getSelectedIndex() == 0 ? loadJson(fileName) : loadCsv(fileName);
                if (performed) {
                    JOptionPane.showMessageDialog(null, fileName + " has been loaded successfully.",
                            "Successfully Loaded.", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,"Load file Unsuccessful."
                                    + " Please check if there is any typo/the file exists/is readable.", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private boolean loadJson(String fileName) {
            try {
                JsonReader reader = new JsonReader("./data/" + fileName);
                Data d = reader.read();
                data.copyFromData(d);
                columnAdded.clear();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        private boolean loadCsv(String fileName) {
            Data d = new Data();
            if (d.importFile(fileName)) {
                data = new Data();
                data.importFile(fileName);
                columnAdded.clear();
                return true;
            } else {
                return false;
            }
        }
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
     * print the Data/newly added columns.
     */
    private class PrintDataAction extends AbstractAction {
        PrintDataAction() {
            super("Print Data/Newly Added Columns?");
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
     * Represents the action to be taken when the user wants to
     * specify data type of each columns
     */
    private class SpecifyTypeAction extends AbstractAction {
        SpecifyTypeAction() {
            super("Specify data types of Columns");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JPanel specifyPanel = new JPanel();
            specifyPanel.setLayout(new GridLayout(2,1));

            String[] colNames = convertNamesToString();
            JComboBox colBox = new JComboBox(colNames);

            String[] dataTypesChoices = {"i: Integer", "d: Double", "s: String", "o: Object"};
            JComboBox dataTypeBox = new JComboBox(dataTypesChoices);

            specifyPanel.add(new JLabel("Select Column:"));
            specifyPanel.add(colBox);
            specifyPanel.add(new JLabel("Select data type to specify:"));
            specifyPanel.add(dataTypeBox);

            String message = "Choose a Column to specify its data type:";
            int result = JOptionPane.showConfirmDialog(null, specifyPanel,
                    message, JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String[] dataTypes = {"i", "d", "s", "o"};
                data.specifyColType(data.getCol(colBox.getSelectedIndex()), dataTypes[dataTypeBox.getSelectedIndex()]);
            }
        }

        private String[] convertNamesToString() {
            String[] s = new String[data.getNumOfCol()];
            for (int i = 0; i < data.getNumOfCol(); i++) {
                s[i] = i + ": " + data.getNames().get(i);
            }
            return s;
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
