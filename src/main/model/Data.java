package model;

import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.util.Scanner;
import java.util.Set;

public class Data {
    //field
    private List<Column> data;
    private Integer numOfCol;
    private Integer numOfRow;
    private List<String> names;

    public Data() {
        data = new LinkedList<>();
        names = new LinkedList<>();
        numOfCol = 0;
        numOfRow = 0;
    }

    public Data(Data d) {
        data = new LinkedList<>();
        data.addAll(d.getData());
        numOfCol = d.getNumOfCol();
        numOfRow = d.getNumOfRow();
    }


    //MODIFIES: this
    //EFFECTS: return true if:
    //          files can be read successfully,
    //          store columns of csv files into this.data,
    //          and also update numOfCol and numOfRow,
    //          Assume: first row is names of columns
    //         return false if files cannot be read successfully
    @SuppressWarnings("methodlength")
    Boolean importFile(String filePath) {
        File file = new File("filePath");
        int r = 0;
        int c = 0;
        if (file.isFile() && file.canRead()) {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                c = 0;
                String line = fileScanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
                while (lineScanner.hasNext()) {
                    if ( r == 0 ) {
                        names.add(lineScanner.next());
                    } else if (r == 1) {
                        Column col = new Column();
                        col.renameCol(names.get(c));
                        col.getCol().add(lineScanner.next());
                        addCol(col);
                    } else {
                        data.get(r).getCol().add(lineScanner.next());
                    }

                }
                lineScanner.close();
                r++;
            }
            fileScanner.close();
        }
        return false;
    }

    //REQUIRES: a string with length = numOfColumn and it only consist of valid datatype initials
    //MODIFIES: this
    //EFFECTS: change actual type of elements of columns
    void specifyColType(Column col, String colType) {
        col.specifyType(colType);
    }

    //REQUIRES: two columns with their types belongs to Number, and a valid operator (+ or - or * or /)
    //MODIFIES: this
    //EFFECT: add resulting columns into data
    void arithmetics(Column col1, String operator, Column col2){
        List<Object> result
    }

    //REQUIRES: two Data object that share the same column with unique elements in it
    //MODIFIES: this
    //EFFECT: add columns of another Data into data
    void concatenate(Data another) {

    }

    //REQUIRES: valid x-axis column that all columns on y-axis has valid data w.r.t that x-axis column,
    //          and string with valid chart types(bar/line)
    //EFFECT: show plots of one or several columns
    void visualize(Column x, Data y, String type) {

    }

    //REQUIRES: a Column object
    //MODIFIES: this
    //EFFECT: add a Column object to data
    void addCol(Column col) {

    }

    //REQUIRES: valid column number (>=0 and < data.numOfColumn())
    //EFFECT: return a required column
    Column getCol(Integer colNum) {
        return data.get(colNum);
    }

    String getColType(Integer colNum) {
        return getCol(colNum).getType();
    }

    //EFFECT: return number of columns of data
    int getNumOfCol() {
        return numOfCol;
    }

    //EFFECT: return number of rows of data
    int getNumOfRow() {
        return numOfRow;
    }

    private List<Column> getData() {
        return data;
    }
}
