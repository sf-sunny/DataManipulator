package ui;

import model.Data;
import model.Column;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DataApp {
    private Data data;
    private Data dataToBeConcat;
    private Scanner input;

    //EFFECTS: run the Data Manipulator Application
    //Reference source: TellerApp: https://github.students.cs.ubc.ca/CPSC210/TellerApp
    public DataApp() {
        runDataApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    //Reference source: TellerApp: https://github.students.cs.ubc.ca/CPSC210/TellerApp
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
    //Reference source: TellerApp: https://github.students.cs.ubc.ca/CPSC210/TellerApp
    private void processCommand(String command) {
        if (command.equals("i")) {
            Data d = new Data();
            doImport(d);
            data = d;
        } else if (command.equals("dt") && !data.isEmpty()) {
            doSpecifyDataTypes();
        } else if (command.equals("ind") && !data.isEmpty()) {
            doSpecifyIndex(data);
        } else if (command.equals("a") && !data.isEmpty()) {
            doArithmetics();
        } else if (command.equals("c") && !data.isEmpty()) {
            doConcatenate();
        } else if (command.equals("col") && !data.isEmpty()) {
            doColumn();
        } else if (command.equals("row")) {
            doRow();
        } else if (command.equals("v") && !data.isEmpty()) {
            doView();
        } else if (data.isEmpty()) {
            System.out.print("Data is Empty. Please import file first.\n");
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes Scanner
    private void init() {
        data = new Data();
        dataToBeConcat = new Data();
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        data.importFile("testing.csv");
        System.out.println("Welcome to Sunny's Data Manipulator!\n");
        System.out.println("File ./data/testing.csv is pre-imported\n");
    }

    // EFFECTS: displays menu of options to user
    //Reference source: TellerApp: https://github.students.cs.ubc.ca/CPSC210/TellerApp
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

    // EFFECTS: import csv files under ./data/ to Data d
    private void doImport(Data d) {
        String fileName = "";

        System.out.print("Enter file name (include .csv) under ./data/ to import: ");
        fileName = input.next();

        while (!d.importFile(fileName) || d.isEmpty()) {
            if (!d.importFile(fileName)) {
                System.out.print("Import Unsuccessful. "
                        + "Please check if there is any typo/the file exists/is readable.\n");
            } else if (d.isEmpty()) {
                System.out.print("Data is Empty. Please import a non-empty file.\n");
            }
            System.out.print("Enter file name (include .csv) under ./data/ to import: ");
            fileName = input.next();
        }
        System.out.print(fileName + " has been imported successfully.\n");
    }

    // EFFECTS: print column names under data and their data types,
    //          and first n rows of Data (if n > d.numOfRow(), n = d.numOfRow())
    private void doView() {
        Integer n = 0;
        n = checkIntInput(data.getNumOfRow() + 1, "number of rows to be printed");
        printColAndType();
        printRows(n);
        printSize();

    }

    //EFFECTS: print column names and their corresponding data type
    private void printColAndType() {
        if (data.getIndex() == -1) {
            System.out.println("\nIndex Column: None");
        } else {
            System.out.println("\nIndex: " + data.getCol(data.getIndex()).getName()
                    + " with Column Number: " + data.getIndex());
        }

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
            System.out.print(sep + s);
            sep = ",";
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            sep = "";
            for (int j = 0; j < data.getNumOfCol(); j++) {
                System.out.print(sep + data.getCol(j).get(i));
                sep = ",";
            }
            System.out.println();
        }
    }

    // EFFECTS: print total size of data
    private void printSize() {
        System.out.println("Data Total Size (row x col): " + data.getNumOfRow() + " x " + data.getNumOfCol());
    }

    // REQUIRES: data initials has to be compatible with elements in column
    // EFFECTS: specify data types on columns
    private void doSpecifyDataTypes() {

        Column col = chooseCol(false);
        String selection = selectDataType();
        data.specifyColType(col, selection);
        System.out.println("Casting Successful.");

    }

    //REQUIRES: data types has to be compatible with elements in column, otherwise the system may quit.
    //EFFECT: return selection of data type
    private String selectDataType() {
        String selection = "";
        while (!selection.equals("i") && !selection.equals("d") && !selection.equals("s") && !selection.equals("o")) {
            System.out.println("Cast Column by the following data type:");
            System.out.println("i -> integer");
            System.out.println("d -> double");
            System.out.println("s -> string");
            System.out.println("o -> Object");
            System.out.println("**data types has to be compatible with elements in column, "
                    + "otherwise the system may quit.");
            selection = input.next();
        }
        return selection;
    }

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

    //REQUIRES: 0 < int max
    //EFFECT: check if the input is integer and within 0 and max-1
    private int checkIntInput(int max, String s) {
        Integer n = -1;
        input.nextLine();
        while (n < 0 || n >= max) {
            System.out.print("Enter " + s + ": ");
            try {
                n = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                n = -1;
            }
            if (n < 0 || n >= max) {
                System.out.print("Please input a valid positive integer within the range.\n");
            }
        }
        return n;

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

    //EFFECTS: specify an Index Column in d
    private void doSpecifyIndex(Data d) {
        System.out.println("Choose a Column be Index.");
        Column col = chooseCol(false);
        while (!col.checkUnique()) {
            System.out.println("Column has duplicate entries, cannot be set as Index.");
            System.out.println("Choose a Column be Index.");
            col = chooseCol(false);
        }
        d.setIndex(col);
        System.out.println("Index has been set successfully.");
    }

    //EFFECT: only append column whose name is not in data.names,
    //          else replace that column (with the same name) in data by the resulting column
    private void doArithmetics() {
        if (!data.checkIfAnyArithmetics()) {
            System.out.println("Sorry, none of the columns are specified as integer or double.");
            System.out.println("Cannot perform arithmetics on it.");
            return;
        }
        System.out.println("Choose Column 1 to do arithmetics.");
        Column col1 = chooseCol(false);
        col1 = checkArithmetic(col1, 1);

        String op = chooseOp();

        System.out.println("Choose Column 2 to do arithmetics.");
        Column col2 = chooseCol(false);
        col2 = checkArithmetic(col2, 2);

        data.arithmetics(col1, op, col2);
        System.out.println("\"" + col1.getName() + op + col2.getName()
                + "\" performed successfully and appended to Data");
    }


    //EFFECTS: return a column which has type == "i" or type == "d"
    private Column checkArithmetic(Column col, int i) {
        while (!col.checkIfArithmetic()) {
            System.out.println("Column is neither integer nor double, cannot perform arithmetics on it.");
            System.out.println("Choose Column " + i + " to do arithmetics.");
            col = chooseCol(false);
        }
        return col;
    }

    //EFFECTS: return an operator (+ or - or * or /)
    private String chooseOp() {
        String op = "";
        while (!op.equals("+") && !op.equals("-") && !op.equals("*") && !op.equals("/")) {
            System.out.println("Choose Operator to do: ");
            System.out.println("+ -> Addition\n- -> Subtraction\n* -> Multiplication\n/ -> Division");
            op = input.next();
        }
        return op;
    }

    //REQUIRES: dataToBeConcat has a column of unique elements such that it can be specified as Index Column
    private void doConcatenate() {
        if (data.getIndex() == -1) { //check if data has Index Column
            System.out.println("Please specify Index Column before concatenating files.");
            return;
        }
        System.out.println("Please import the file to be concatenated to current Data."); //import file to be concat
        doImport(dataToBeConcat);

        System.out.println("Please specify the Index Column of the file to be concatenated to current Data.");
        doSpecifyIndex(dataToBeConcat);
        if (!data.concatenate(dataToBeConcat)) {
            System.out.println("Sorry. Index Column of new files seems "
                    + "does not match the Index Column of current Data.");
            System.out.println("Concatenation unsuccessful");
            return;
        }
        System.out.println("Concatenation successful.");

    }

    //EFFECTS: add or remove a Column, by Object or Index
    private void doColumn() {
        String s = "";

        while (!s.equals("a") && !s.equals("r") && !s.equals("m")) {
            System.out.println("Operation on Column:\na -> add a column\nr -> remove a Column\nm -> return to menu");
            s = input.next();
        }
        if (s.equals("r")) {
            chooseCol(true);
            System.out.println("Column is successfully removed.");
        } else if (s.equals("a")) {
            constructCol();
        }

    }

    //REQUIRES: data types has to be compatible with elements in column, otherwise the system may quit.
    //EFFECT: only append column whose name is not in data.names,
    //         else replace that column (with the same name) in data by the new column
    private void constructCol() {
        int index = data.getIndex();
        String s = "";
        String type = "";
        String o = "";
        Column newCol = new Column();
        System.out.println("Please enter the name of new column.");
        s = input.next();
        type = selectDataType();
        newCol.renameCol(s);

        for (int i = 0; i < data.getNumOfRow(); i++) {
            if (index != -1) {
                System.out.print(data.getCol(index).get(i) + ": ");
            } else {
                System.out.print("Entry" + i + ": ");
            }
            o = input.next();
            newCol.addElement(o);
        }
        newCol.specifyType(type);
        data.addCol(newCol);
        System.out.println("Column successfully created and added to Data.");
    }

    //EFFECTS: add or remove a Row, by Object or Index
    private void doRow() {
        String s = "";

        while (!s.equals("a") && !s.equals("r") && !s.equals("m")) {
            System.out.println("Operation on Row:\na -> add a row\nr -> remove a row\nm -> return to menu");
            s = input.next();
        }
        if (s.equals("r")) {
            removeRow();
            System.out.println("Row is successfully removed.");
        } else if (s.equals("a")) {
            constructRow();
        }

    }

    //EFFECTS: remove a row
    private void removeRow() {
        String selection = "";
        while (!selection.equals("i") && !(selection.equals("n") && data.getIndex() != -1)) {
            System.out.println("Choose Row by:");
            System.out.println("i -> integer index");
            System.out.println("n -> elements in Index Column");
            selection = input.next();
            if (selection.equals("n") && data.getIndex() == -1) {
                System.out.println("Please specify Index Column first before removing row by index.");
            }
        }

        if (selection.equals("i")) {
            removeRowByInt();
        } else if (selection.equals("n") && data.getIndex() != -1) {
            removeRowByName();
        }
    }

    //EFFECTS: remove a row by its integer index
    private void removeRowByInt() {
        int n = -1;
        while (n < 0) {
            n = checkIntInput(data.getNumOfRow(), "integer index of row");
        }
        data.removeRow(n);
    }

    //EFFECTS: remove a row by its Name in Index Column
    private void removeRowByName() {
        String s = "";
        while (!data.getCol(data.getIndex()).contains(s)) {
            System.out.print("Enter name of Row: ");
            s = input.next();
            if (!data.getCol(data.getIndex()).contains(s)) {
                System.out.println("Invalid name.");
            }
        }
        data.removeRow(s);
    }

    //REQUIRES: data types has to be compatible with elements in column, otherwise the system may quit.
    //EFFECT: only append column whose name is not in data.names,
    //         else replace that column (with the same name) in data by the new column
    private void constructRow() {
        String s = "";
        List<Object> list = new ArrayList<>();

        System.out.println("**data types has to be compatible with elements in column, otherwise the system may quit.");
        for (int i = 0; i < data.getNumOfCol(); i++) {
            System.out.print(data.getCol(i).getName() + " (type: " + data.getCol(i).getType() + "): ");
            s = input.next();
            list.add(s);
        }
        data.addRow(list);

        System.out.println("Row is successfully created and added to Data.");
    }
}
