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

    //EFFECTS: create an empty Data with index = -1 (means index hasn't been assigned)
    public Data() {
        data = new LinkedList<>();
        names = new LinkedList<>();
        numOfCol = 0;
        numOfRow = 0;
        index = -1;
    }

    //REQUIRES: a valid Data d
    //MODIFIES: this
    //EFFECTS: make a copy from another Data d
    public void copyFromData(Data d) {
        data = new LinkedList<>();
        data.addAll(d.getData());
        names = new LinkedList<>();
        names.addAll(d.getNames());
        numOfCol = d.getNumOfCol();
        numOfRow = d.getNumOfRow();
        index = d.getIndex();
    }


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



    //MODIFIES: this
    //EFFECTS: return true if:
    //          files can be read successfully,
    //          store columns of csv files into this.data,
    //          and also update numOfCol and numOfRow,
    //          Assume: first row is names of columns
    //         return false if files cannot be read successfully
    public Boolean importFile(String filePath) {
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

    //REQUIRES: working lineScanner, correct row number r
    //EFFECTS: skip any string starts with UTF8_BOM,
    //          for the first row (r==0), create and rename a new Column, then add to this.data
    //          for other rows, add elements to corresponding column
    //          return the total number of columns
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


    //REQUIRES: a string valid datatype initials
    //MODIFIES: this
    //EFFECTS: change actual type of elements of columns
    public void specifyColType(Column col, String colType) {
        col.specifyType(colType);
    }

    //REQUIRES: a list with length == numOfColumn,
    //          and elements of it should the compatible to corresponding column's datatype
    //MODIFIES: this
    //EFFECTS: add a row at the bottom of the Data,
    //         and specify every elements according to corresponding column's datatype
    public void addRow(List<Object> list) {
        int i = 0;
        for (Column col:data) {
            col.addElement(list.get(i));
            i++;
        }
        numOfRow++;
    }


    //REQUIRES: a String name that exist in this.names
    //EFFECT: remove the first occurring column with its column.name() == name
    public void removeRow(int i) {
        for (Column col:data) {
            col.remove(i);
        }
        numOfRow--;
    }

    //REQUIRES: an Object o that exist in index column (data.getCol(index)),
    //          0 < index < numOfCol
    //MODIFIES: this
    //EFFECT: remove the first occurring row with its index == o
    public void removeRow(Object o) {
        int i = getCol(this.index).getIndexByObject(o);
        removeRow(i);
    }

    //REQUIRES: two columns with their types belongs to Number, and a valid operator(op) (+ or - or * or /)
    //MODIFIES: this
    //EFFECT: add resulting columns into data if operator is valid, else do nothing
    public void arithmetics(Column col1, String op, Column col2) {
        Column result;
        if (op.equals("+")) {
            result = col1.add(col2);
        } else if (op.equals("-")) {
            result = col1.minus(col2);
        } else if (op.equals("*")) {
            result = col1.multiply(col2);
        } else if (op.equals("/")) {
            result = col1.divideDouble(col2);
        } else {
            return;
        }
        addCol(result);
    }

    //REQUIRES: two Data object that share the same index column
    //          (orders can be different, but size and elemnts have to be the same),
    //MODIFIES: this
    //EFFECT: add columns of another Data into data if there is no the same column name in data,
    //          otherwise if there is the same column name in another,
    //          replace that column which has the same name in data by the column in another
    //        If the above actions can be done, return true, otherwise return false
    public boolean concatenate(Data another) {
        if (getCol(index).checkEqualsUnique(another.getCol(another.getIndex()))) {
            List<Integer> positionInAnother = checkIndexPositionInAnother(another);
            int tmp = 0;
            for (Column col:another.getData()) { //loop through columns in another
                if (tmp != another.getIndex()) {
                    Column newCol = new Column(col.getName(), new LinkedList<>(), col.getType());
                    newCol.insertElementInOrder(col, positionInAnother);
                    addCol(newCol);
                }
                tmp++;
            }
            return true;
        }
        return false;
    }

    //REQUIRES: Index column of another should have same and unique elements as Index column of this.column
    //EFFECTS: return a list of position of elements in Index column of another
    //          (w.r.t. Index column ofthis.column)
    public List<Integer> checkIndexPositionInAnother(Data another) {
        Column colAnother = another.getCol(another.getIndex());
        Column col = getCol((getIndex()));
        return col.checkPositionInAnother(colAnother);
    }


    //REQUIRES: a Column object which has the length == numOfRow
    //MODIFIES: this
    //EFFECT: if a col.name is in data.names, replace that column in data by col
    //          else, add a Column object to data.
    public void addCol(Column col) {
        if (names.contains(col.getName())) {
            int i = names.indexOf(col.getName());
            data.set(i, col);
        } else {
            data.add(col);
            names.add(col.getName());
            numOfCol++;
        }
    }

    //REQUIRES: 0 <= colNum < numOfCol
    //MODIFIES: this
    //EFFECT: remove a Column object, data[index], in data
    public void removeCol(int colNum) {
        if (colNum == index) {
            index = -1;
        } else if (colNum < index) {
            index--;
        }
        names.remove(data.get(colNum).getName());
        data.remove(colNum);
        numOfCol--;

        if (getNumOfCol() == 0) {
            numOfRow = 0;
        }
    }


    //REQUIRES: a String name that exist in this.names
    //EFFECT: remove the first occurring column with its column.name() == name
    public void removeCol(String name) {
        int colNum = names.indexOf(name);
        removeCol(colNum);
    }

    //REQUIRES: valid column number (>=0 and < data.numOfColumn())
    //EFFECT: return a required column
    public Column getCol(int colNum) {
        return data.get(colNum);
    }

    //REQUIRES: a String name that exist in this.names
    //EFFECT: return the first occuring column with its column.name() == name
    public Column getCol(String name) {
        int colNum = names.indexOf(name);
        return getCol(colNum);
    }

    //REQUIRES: valid column number (>=0 and < data.numOfColumn())
    //EFFECT: return initials of datatype of that column
    public String getColType(Integer colNum) {
        return getCol(colNum).getType();
    }

    //REQUIRES: valid column name (exist in this.names)
    //EFFECT: return initials of datatype of the first column which has the same name as name
    public String getColType(String name) {
        int colNum = names.indexOf(name);
        return getColType(colNum);
    }

    //EFFECT: return number of columns of data
    public int getNumOfCol() {
        return numOfCol;
    }

    //EFFECT: return number of rows of data
    public int getNumOfRow() {
        return numOfRow;
    }

    //EFFECT: return column number of index
    public int getIndex() {
        return index;
    }

    //EFFECT: return a list of columns of data
    private List<Column> getData() {
        return data;
    }

    //EFFECT: return a list of names of columns in data
    public List<String> getNames() {
        return names;
    }

    //EFFECT: return true if Data is empty
    public boolean isEmpty() {
        return getNumOfCol() == 0;
    }

    //EFFECT: return true if any column in data has type == "i" or type == "d"
    public boolean checkIfAnyArithmetics() {
        for (Column col:getData()) {
            if (col.checkIfArithmetic()) {
                return true;
            }
        }
        return false;
    }


}
