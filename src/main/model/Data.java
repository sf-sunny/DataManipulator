package model;

import java.util.LinkedList;
import java.util.List;

public class Data {
    //field
    private List<Column> data;
    private Integer numOfColumn;
    private Integer numOfRow;

    public Data() {
        data = new LinkedList<>();
        numOfColumn = 0;
        numOfRow = 0;
    }


    //MODIFIES: this
    //EFFECTS: return true if:
    //          files can be read successfully,
    //          and store columns of csv files into this.data,
    //          and also update numOfCol and numOfRow
    //         return false if files cannot be read successfully
    Boolean importFile(String filePath) {
        return false;
    }

    //REQUIRES: a string with length = numOfColumn and it only consist of valid datatype initials
    //MODIFIES: this
    //EFFECTS: change actual type of elements of columns
    void specifyColType(String colTypes) {

    }

    //REQUIRES: two columns with their types belongs to Number, and a valid operator with length == 1
    //MODIFIES: this
    //EFFECT: add resulting columns into data
    void arithmetics(Column col1, String operator, Column col2){

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

    //EFFECT: return number of columns of data
    int getNumberOfCol() {
        return numOfColumn;
    }

    //EFFECT: return number of rows of data
    int getNumberOfRow() {
        return numOfRow;
    }
}
