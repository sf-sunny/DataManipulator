package model;

import java.util.LinkedList;
import java.util.List;

public class Column {
    private List<Object> column;
    private String name;
    private String type;
    //type can be "i" = Integer, "d" = Double, "s" = String, "o" = Object(unspecified/default)
    private Integer size;

    public Column() {
        column = new LinkedList<>();
        name = "";
        type = "o";
        size = 0;
    }

    //REQUIRES: valid datatype initials ("i" = Integer, "d" = Double, "s" = String, "o" = Object),
    //          and the datatype should match the actual data in Column
    //          e.g. this function will not function properly
    //          while specifying "i" over a column full of characters and strings
    //          e.g.2. if object 1.1 is casted as integer, then the element will be changed to 1
    //MODIFIES: this
    //EFFECTS: change the actual type of all elements in column
    public void specifyType(String colType) {
        if (colType == "i") {
            type = "i";
            for (Object o : column) {
                //System.out.println(o.getClass().getName());
                String s = o.toString();
                o = (int) Double.parseDouble(s);
            }
        } else if (colType == "d") {
            type = "d";
            for (Object o : column) {
                String s = o.toString();
                o = Double.valueOf(s);
            }
        } else if (colType == "s") {
            type = "s";
            for (Object o : column) {
                o = o.toString();
            }
        } else if (colType == "o") {
            type = "o";
        }
    }

    //REQUIRES: a valid string
    //MODIFIES: this
    //EFFECTS: change the name of a Column
    public void renameCol(String newName) {
        name = newName;
    }


    //REQUIRES: a valid list with the supertype of elements in List is Object
    //MODIFIES: this
    //EFFECTS: assign a list to this.column
    public void listAsCol(List<Object> list) {
        List<Object> newList = new LinkedList<>();
        newList.addAll(list);
        column = newList;
        size = list.size();
    }


    //REQUIRES: a list whose elements have same type as elements in this.column
    //MODIFIES: this
    //EFFECTS: append this.column with col, with all elements casted as column.type
    // type?????
    public void appendCol(Column col) {
        column.addAll(col.getCol());
        this.specifyType(type);
        size += col.getSize();
    }


    //REQUIRES: this.column and another column has same length, and they are addable (either Integer or Double),
    //             and resultant sum of each element are within the range of their data type
    //EFFECTS: return a Column with sum of two columns
    public Column add(Column another) {
        List<Object> anotherList = another.getCol();
        List<Object> finalList = new LinkedList<>();
        Boolean bothInt;

        if ((another.getType() == "i") && (type == "i")) {
            bothInt = true;
        } else {
            bothInt = false;
        }

        int i = 0;
        for (Object o:column) {
            if (bothInt) {
                finalList.add(Integer.parseInt(o.toString()) + Integer.parseInt(anotherList.get(i).toString()));
            } else {
                finalList.add(Double.parseDouble(o.toString()) + Double.parseDouble(anotherList.get(i).toString()));
            }
            i++;
        }

        Column finalCol = new Column();
        finalCol.listAsCol(finalList);
        return finalCol;
    }

    //REQUIRES: this.column and another column has same length, and they are subtractable (either Integer or Double),
    //            and resultant difference of each element are within the range of their data type
    //EFFECTS: return a Column with elements of this.column - elements of another.column
    public Column minus(Column another) {
        List<Object> anotherList = another.getCol();
        List<Object> finalList = new LinkedList<>();
        Boolean bothInt;

        if ((another.getType() == "i") && (type == "i")) {
            bothInt = true;
        } else {
            bothInt = false;
        }

        int i = 0;
        for (Object o:column) {
            if (bothInt) {
                finalList.add(Integer.parseInt(o.toString()) - Integer.parseInt(anotherList.get(i).toString()));
            } else {
                finalList.add(Double.parseDouble(o.toString()) - Double.parseDouble(anotherList.get(i).toString()));
            }
            i++;
        }

        Column finalCol = new Column();
        finalCol.listAsCol(finalList);
        return finalCol;
    }

    //REQUIRES: this.column and another column has same length, and they are multipliable (either Integer or Double),
    //            and resultant product of each element are within the range of their data type
    //EFFECTS: return a Column with product of two columns
    public Column multiply(Column another) {
        List<Object> anotherList = another.getCol();
        List<Object> finalList = new LinkedList<>();
        Boolean bothInt;

        if ((another.getType() == "i") && (type == "i")) {
            bothInt = true;
        } else {
            bothInt = false;
        }

        int i = 0;
        for (Object o:column) {
            if (bothInt) {
                finalList.add(Integer.parseInt(o.toString()) * Integer.parseInt(anotherList.get(i).toString()));
            } else {
                finalList.add(Double.parseDouble(o.toString()) * Double.parseDouble(anotherList.get(i).toString()));
            }
            i++;
        }

        Column finalCol = new Column();
        finalCol.listAsCol(finalList);
        return finalCol;
    }

    //REQUIRES: this.column and another column has same length, and they are divisible (either Integer or Double),
    //            resultant quotient of elements of this.column divided by elements of another column,
    //            results are within the range of double,
    //            and another column should not contain any 0 element
    //EFFECTS: return a Column with quotients, stored as Double,
    //          of elements of this.column divided by elements of another column.
    public Column divideDouble(Column another) {
        List<Object> anotherList = another.getCol();
        List<Object> finalList = new LinkedList<>();

        int i = 0;
        for (Object o:column) {
                finalList.add(Double.parseDouble(o.toString()) / Double.parseDouble(anotherList.get(i).toString()));
            i++;
        }

        Column finalCol = new Column();
        finalCol.listAsCol(finalList);
        return finalCol;
    }

    //EFFECTS: return name of column
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getSize() {
        return size;
    }

    public Object get(Integer index) {
        Object o = column.get(index);
        return o;
    }

    public List<Object> getCol() {
        List<Object> l = new LinkedList<>();
        l.addAll(column);
        return l;
    }


    //EFFECTS: return True if actual type of elements of column is Integer or Double,
    //          return False otherwise.
    public Boolean checkIfArithmetic() {
        return ((type == "i") || (type == "d"));
    }
}
