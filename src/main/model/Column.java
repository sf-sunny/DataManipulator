package model;

import java.util.LinkedList;
import java.util.List;

public class Column {
    private List<Object> column;
    private String name;

    public Column() {
        column = new LinkedList<>();
    }

    //REQUIRES: valid datatype initials
    //MODIFIES: this
    //EFFECTS: change the actual type of all elements in column
    public void specify(String colType) {

    }

    //REQUIRES: this.column and another column has same length, and they are addable,
    //             and resultant sum of each element are within the range of their data type
    //EFFECTS: return a Column with sum of two columns
    public Column add() {
        return new Column();
    }

    //REQUIRES: this.column and another column has same length, and they are subtractable,
    //            and resultant difference of each element are within the range of their data type
    //EFFECTS: return a Column with difference of two columns
    public Column minus() {
        return new Column();
    }

    //REQUIRES: this.column and another column has same length, and they are multipliable,
    //            and resultant product of each element are within the range of their data type
    //EFFECTS: return a Column with product of two columns
    public Column multiply() {
        return new Column();
    }

    //REQUIRES: this.column and another column has same length, and they are divisible,
    //            and resultant quotient of elements of this.column divided by elements of another column
    //            are within the range of double
    //EFFECTS: return a Column with quotients, stored as Double,
    //          of elements of this.column divided by elements of another column
    public Column divideDouble() {
        return new Column();
    }

    //EFFECTS: return name of column
    public String getName() {
        return name;
    }
}
