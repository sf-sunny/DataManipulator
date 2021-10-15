package ui;

import model.Data;
import model.Column;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DataApp {
    private Data data;
    private Scanner input;

    //EFFECTS: run the Data Manipulator Application
    public DataApp() {
        runDataApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runDataApp() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("i")) {
            doImport();
        } else if (command.equals("dt")) {
            doSpecifyDataTypes();
//        } else if (command.equals("ind")) {
//            doSpecifyIndex();
//        } else if (command.equals("a")) {
//            doArithmetics();
//        } else if (command.equals("c")) {
//            doConcatenate();
//        } else if (command.equals("col")) {
//            doColumn();
//        } else if (command.equals("row")) {
//            doRow();
       } else if (command.equals("v")) {
            doView();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes Scanner
    private void init() {
        data = new Data();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        data.importFile("testing.csv");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ti -> import csv files (under ./data/)");
        System.out.println("\tdt -> specify data types of Columns");
        System.out.println("\tind -> specify Index column");
        System.out.println("\ta -> do arithmetics (+,-,*,/)");
        System.out.println("\tc -> concatenate csv files (under ./data/)"); //do import, Index
        System.out.println("\tcol -> add/remove Column");
        System.out.println("\trow -> add/remove row (entry)");
        System.out.println("\tv -> view Data");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: import csv files under ./data/ to Data
    private void doImport() {
        String fileName = "";

        System.out.print("Enter file name (include .csv) under ./data/ to import: ");
        fileName = input.next();
        if (data.importFile(fileName)) {
            System.out.print(fileName + " has been imported successfully.\n");
        } else {
            System.out.print("Import Unsuccessful. Please check if there is any typo/the file exists/is readable.\n");
        }
    }

    // EFFECTS: print column names under data and their data types,
    //          and first n rows of Data (if n > d.numOfRow(), n = d.numOfRow())
    private void doView() {
        if (data.isEmpty()) {
            System.out.print("Data is Empty. Please import file.\n");
            return;
        } else {
            Integer n = 0;
            while (n <= 0) {
                System.out.print("Enter number of rows to be printed: ");
                try {
                    n = input.nextInt();
                } catch (InputMismatchException e) {
                    System.out.print("Please input a valid positive integer.\n");
                    return;
                }
                if (n <= 0) {
                    System.out.print("Please input a valid positive integer.\n");
                }
            }

            printColAndType();
            printRows(n);
            printSize();
        }
    }

    //EFFECTS: print column names and their corresponding data type
    private void printColAndType() {
        System.out.println("Column Name, Data Type (i: int, d: double, s: string, o: object (default)");
        for (int i = 0; i < data.getNumOfCol(); i++) {
            System.out.println(data.getCol(i).getName() + "," + data.getCol(i).getType());
        }
    }

    //EFFECTS: print first n rows of Data (if n > d.numOfRow(), n = d.numOfRow())
    private void printRows(int n) {
        if (n > data.getNumOfRow()) {
            n = data.getNumOfRow();
        }
        List<String> names = data.getNames();
        String sep = "";
        for (String s:names) {
            System.out.print(sep+s);
            sep = ",";
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            sep = "";
            for (int j = 0; j < data.getNumOfCol(); j++) {
                System.out.print(sep + data.getCol(j).get(i) );
                sep = ",";
            }
            System.out.println();
        }
    }

    // EFFECTS: print total size of data
    private void printSize() {
        System.out.println("Data Total Size (row x col): " + data.getNumOfRow() + " x " + data.getNumOfCol());
    }
    // EFFECTS: specify data types on columns
    private void doSpecifyDataTypes() {
        Column = chooseCol();

    }

    private Column chooseCol() {
        String selection = "";
        while (!selection.equals("i") || !selection.equals("n")) {
            System.out.println("Choose Column by:");
            System.out.println("i -> integer index");
            System.out.println("n -> name of Column");
            selection = input.next();
        }

        if (selection.equals("i")) {
            return chooseColByInt();
        } else return chooseColByName();
    }

    private Column chooseColByInt() {
        int n = -1;
        while (n < 0) {
            n = checkIntInput(data.getNumOfCol(), "integer index of row");
        }
        return data.getCol(n);
    }

    private int checkIntInput(int max, String s) {
        Integer n = 0;
        while (n <= 0) {
            System.out.print("Enter " + s + ": ");
            try {
                n = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Please input a valid positive integer.\n");
                return -1;
            }
            if (n <= 0 || n >= max) {
                System.out.print("Integer out of range.\n");
            }
        }

    }


//    doSpecifyDataTypes();
//
//    doSpecifyIndex();
//
//    doArithmetics();
//
//    doConcatenate();
//
//    //add/remove, by Object or by Index
//    doColumn();
//
//    //add/remove, by Object or by Index
//    doRow();

}
