package model;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.util.Scanner;

public class Data {
    //field
    private List<Column> data;
    private Integer numOfCol;
    private Integer numOfRow;
    private List<String> names;
    private Integer index; //-1 means index has not been set
    private static final String UTF8_BOM = "\uFEFF";

    public Data() {
        data = new LinkedList<>();
        names = new LinkedList<>();
        numOfCol = 0;
        numOfRow = 0;
        index = -1;
    }

    public Data(Data d) {
        data = new LinkedList<>();
        data.addAll(d.getData());
        numOfCol = d.getNumOfCol();
        numOfRow = d.getNumOfRow();
        index = -1;
    }

    //NOT
    //REUQIRES: a column exist in data.column
    //MODIFIES: this
    //EFFECTS: if all elements in col are unique,
    //         set this.index = names.indexOf(col.getName())
    //          and return true,
    //          otherwise return false
    public boolean setIndex(Column col) {
        if (col.checkUnique()) {
            index = names.indexOf(col.getName());
            return true;
        }
        return false;
    }


    // functions to be added:
    //setIndex (assume unique) done
    //addRow done
    //delRow
    //delCol done
    //getColByName (assume column names are unique) done
    //del(Element) done
    //add(Element) done
    //specifyElement done

    //MODIFIES: this
    //EFFECTS: return true if:
    //          files can be read successfully,
    //          store columns of csv files into this.data,
    //          and also update numOfCol and numOfRow,
    //          Assume: first row is names of columns
    //         return false if files cannot be read successfully
    @SuppressWarnings("methodlength")
    Boolean importFile(String filePath) {
        File file = new File("data/" + filePath);
        int r = 0;
        int c = 0;
 //       if (file.isFile() && file.canRead()) {
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(",");
            c = scanItemsInLine(lineScanner, r);
            lineScanner.close();
            r++;
        }
        fileScanner.close();
        numOfCol = c;
        numOfRow = r - 1;
        return true;
    }

    private int scanItemsInLine(Scanner lineScanner, int r) {
        int c = 0;
        while (lineScanner.hasNext()) {
            String item = lineScanner.next();
            if (item.startsWith(UTF8_BOM)) {
                item = item.substring(1);
            }
            if (r == 0) {
                Column col = new Column();
                col.renameCol(item);
                addCol(col);
            } else {
                getCol(c).addElement(item);
            }
            c++;
        }
        return c;
    }


    //REQUIRES: a string with length == numOfColumn and it only consist of valid datatype initials
    //MODIFIES: this
    //EFFECTS: change actual type of elements of columns
    void specifyColType(Column col, String colType) {
        col.specifyType(colType);
    }

    //REQUIRES: a list with length == numOfColumn,
    //          and elements of it should the compatible to corresponding column's datatype
    //MODIFIES: this
    //EFFECTS: add a row at the bottom of the Data,
    //         and specify every elements according to corresponding column's datatype
    void addRow(List<Object> list) {
        int i = 0;
        for (Column col:data) {
            col.addElement(list.get(i));
            i++;
        }
        numOfRow++;
    }

    //NOT TESTED
    //REQUIRES: a String name that exist in this.names
    //EFFECT: remove the first occurring column with its column.name() == name
    void removeRow(int i) {
        for (Column col:data) {
            col.remove(i);
        }
        numOfRow--;
    }

    //REQUIRES: an Object o that exist in index column (data.getCol(index)),
    //          0 < index < numOfCol
    //MODIFIES: this
    //EFFECT: remove the first occurring row with its index == o
    void removeRow(Object o) {
        int i = getCol(this.index).getIndexByObject(o);
        removeRow(i);
    }

    //REQUIRES: two columns with their types belongs to Number, and a valid operator(op) (+ or - or * or /)
    //MODIFIES: this
    //EFFECT: add resulting columns into data
    void arithmetics(Column col1, String op, Column col2) {
        Column result = new Column();
        if (op.equals("+")) {
            result = col1.add(col2);
        } else if (op.equals("-")) {
            result = col1.minus(col2);
        } else if (op.equals("*")) {
            result = col1.multiply(col2);
        } else if (op.equals("/")) {
            result = col1.divideDouble(col2);
        }
        addCol(result);
    }

    //REQUIRES: two Data object that share the same index column,
    //
    //MODIFIES: this
    //EFFECT: add columns of another Data into data
    void public Boolean concatenate(Data another) {
        if (getCol(index).checkEqualsUnique(another.getCol(another.getIndex()))) {
            list<int> positionInAnother =
            int tmp = 0;
            for (Column col:another.getData()) { //loop through columns in another
                if (tmp != another.getIndex()) {

                    Column newCol = new Column(col.getName(), new LinkedList<>(), col.getType());

                    for (Object o : getCol(this.index).getColAsList()) {
                        int i = col.get(another.getIndex()).getIndexByObject(o);
                        newCol.add(col)
                    }
                    addCol(newCol);
                }
            }
        }
        return false;

    }

    void private List<int> checkPositionInAnother() {

    }

//    //REQUIRES: valid x-axis column that all columns on y-axis has valid data w.r.t that x-axis column,
//    //          and string with valid chart types(bar/line)
//    //EFFECT: show plots of one or several columns
//    void visualize(Column x, Data y, String type) {
//
//    }

    //REQUIRES: a Column object which has the length == numOfRow
    //MODIFIES: this
    //EFFECT: if a col.name is in data.names, replace that column in data by col
    //          else, add a Column object to data.
    void addCol(Column col) {
        if (names.contains(col.getName())) {
            int i = names.indexOf(col.getName());
            data.set(i, col);
        } else {
            data.add(col);
            names.add(col.getName());
            numOfCol++;
        }
    }

    //NOT TESTED
    //REQUIRES: 0 <= colNum < numOfCol
    //MODIFIES: this
    //EFFECT: remove a Column object, data[index], in data
    void removeCol(int colNum) {
        if (colNum == index) {
            index = -1;
        } else if (colNum < index) {
            index--;
        }
        names.remove(data.get(colNum).getName());
        data.remove(colNum);
        numOfCol--;
    }


    //REQUIRES: a String name that exist in this.names
    //EFFECT: remove the first occurring column with its column.name() == name
    void removeCol(String name) {
        int colNum = names.indexOf(name);
        removeCol(colNum);
    }

    //REQUIRES: valid column number (>=0 and < data.numOfColumn())
    //EFFECT: return a required column
    Column getCol(int colNum) {
        return data.get(colNum);
    }

    //REQUIRES: a String name that exist in this.names
    //EFFECT: return the first occuring column with its column.name() == name
    Column getCol(String name) {
        int colNum = names.indexOf(name);
        return getCol(colNum);
    }

    String getColType(Integer colNum) {
        return getCol(colNum).getType();
    }

    String getColType(String name) {
        int colNum = names.indexOf(name);
        return getColType(colNum);
    }

    //EFFECT: return number of columns of data
    int getNumOfCol() {
        return numOfCol;
    }

    //EFFECT: return number of rows of data
    int getNumOfRow() {
        return numOfRow;
    }

    int getIndex() {
        return index;
    }

    private List<Column> getData() {
        return data;
    }
}
