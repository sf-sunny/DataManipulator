package model;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import persistence.Writable;
//test
// Represents a column from a .csv file
public class Column implements Writable {
    private List<Object> column;
    private String name;
    private String type;
    //type can be "i" = Integer, "d" = Double, "s" = String, "o" = Object(unspecified/default)
    private Integer size;

    //EFFECTS: construct a empty Column with type="o", name = ""
    public Column() {
        column = new LinkedList<>();
        name = "";
        type = "o";
        size = 0;
    }


    //REQUIRES: non-empty string on name,
    //          Apparent type of elemnts in List has be Object
    //          string type has to be a valid datatype initials
    //          ("i" = Integer, "d" = Double, "s" = String, "o" = Object),
    //          and the datatype should match the actual data in Column
    //          e.g. this function will not function properly
    //          while specifying "i" over a column full of characters and strings
    //          e.g.2. if object 1.1 is casted as integer, then the element will be changed to 1
    //EFFECTS: construct a Column
    public Column(String name, List<Object> list, String type) {
        listAsCol(list);
        this.name = name;
        specifyType(type);
    }

    //REQUIRES: column.type != d or column.type != o
    //MODIFIES: this
    //EFFECTS: if all elements in column are unique, return true,
    //          otherwise return false
    public boolean checkUnique() {
        Set<Object> set = new TreeSet<>();
        for (Object o: getColAsList()) {
            if (!set.add(o)) {
                return false;
            }
        }
        return true;
    }

    //REQUIRES: valid datatype initials ("i" = Integer, "d" = Double, "s" = String, "o" = Object),
    //          and the datatype should match the actual data in Column
    //          e.g. this function will not function properly
    //          while specifying "i" over a column full of characters and strings
    //          e.g.2. if object 1.1 is casted as integer, then the element will be changed to 1
    //MODIFIES: this
    //EFFECTS: change the actual type of all elements in column
    public void specifyType(String colType) {
        type = colType;
        for (int i = 0; i < size; i++) {
            specify(i, colType);
        }
    }

    //REQUIRES: String colType equals to datatype initials "i" or "d" or "s" or "o"
    //MODIFIES: this
    //EFFECTS: cast type of the Object in as colType
    public void specify(int index, String colType) {
        if (colType.equals("i")) {
            column.set(index, (int) Double.parseDouble(column.get(index).toString()));
        } else if (colType.equals("d")) {
            column.set(index, Double.valueOf(column.get(index).toString()));
        } else if (colType.equals("s")) {
            column.set(index, column.get(index).toString());
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
    public void appendCol(Column col) {
        column.addAll(col.getColAsList());
        this.specifyType(type);
        size += col.getSize();
    }


    //REQUIRES: 0 < index < this.size()
    //MODIFIES: this
    //EFFECTS: remove the element at column[index], this.size--
    public void remove(int index) {
        column.remove(index);
        size--;
    }

    //REQUIRES: Object whose Actual type should be compatible with this.type
    //MODIFIES: this
    //EFFECTS: add the element to the end of column and cast its type to this.type
    public void addElement(Object o) {
        column.add(o);
        specify(getSize(), getType());
        size++;
    }

    //REQUIRES: this.column and another column has same length, and they are addable (either Integer or Double),
    //             and resultant sum of each element are within the range of their data type
    //EFFECTS: return a Column with sum of two columns
    public Column add(Column another) {
        List<Object> anotherList = another.getColAsList();
        List<Object> finalList = new LinkedList<>();
        Boolean bothInt;
        if ((another.getType().equals("i")) && (type.equals("i"))) {
            bothInt = true;
        } else {
            bothInt = false;
        }
        int i = 0;
        for (Object o:column) {
            finalList.add(bothInt ? Integer.parseInt(o.toString()) + Integer.parseInt(anotherList.get(i).toString()) :
                    Double.parseDouble(o.toString()) + Double.parseDouble(anotherList.get(i).toString()));
            i++;
        }

        Column finalCol = new Column(this.name + "+" + another.getName(), finalList, bothInt ? "i" : "d");

        return finalCol;
    }

    //REQUIRES: this.column and another column has same length, and they are subtractable (either Integer or Double),
    //            and resultant difference of each element are within the range of their data type
    //EFFECTS: return a Column with elements of this.column - elements of another.column
    public Column minus(Column another) {
        List<Object> anotherList = another.getColAsList();
        List<Object> finalList = new LinkedList<>();
        Boolean bothInt;

        if ((another.getType().equals("i")) && (type.equals("i"))) {
            bothInt = true;
        } else {
            bothInt = false;
        }

        int i = 0;
        for (Object o:column) {
            finalList.add(bothInt ? Integer.parseInt(o.toString()) - Integer.parseInt(anotherList.get(i).toString()) :
                    Double.parseDouble(o.toString()) - Double.parseDouble(anotherList.get(i).toString()));
            i++;
        }

        Column finalCol = new Column(this.name + "-" + another.getName(), finalList, bothInt ? "i" : "d");
        return finalCol;
    }

    //REQUIRES: this.column and another column has same length, and they are multipliable (either Integer or Double),
    //            and resultant product of each element are within the range of their data type
    //EFFECTS: return a Column with product of two columns
    public Column multiply(Column another) {
        List<Object> anotherList = another.getColAsList();
        List<Object> finalList = new LinkedList<>();
        Boolean bothInt;

        if ((another.getType().equals("i")) && (type.equals("i"))) {
            bothInt = true;
        } else {
            bothInt = false;
        }

        int i = 0;
        for (Object o:column) {
            finalList.add(bothInt ? Integer.parseInt(o.toString()) * Integer.parseInt(anotherList.get(i).toString()) :
                    Double.parseDouble(o.toString()) * Double.parseDouble(anotherList.get(i).toString()));
            i++;
        }

        Column finalCol = new Column(this.name + "*" + another.getName(), finalList, bothInt ? "i" : "d");
        return finalCol;
    }

    //REQUIRES: this.column and another column has same length, and they are divisible (either Integer or Double),
    //            resultant quotient of elements of this.column divided by elements of another column,
    //            results are within the range of double
    //EFFECTS: return a Column with quotients, stored as Double,
    //          of elements of this.column divided by elements of another column.
    //          Note that the effect of dividing by 0 is the same as applying "/" operator to float number
    public Column divideDouble(Column another) {
        List<Object> finalList = new LinkedList<>();

        int i = 0;
        for (Object o:column) {
            finalList.add(Double.parseDouble(o.toString()) / Double.parseDouble(another.get(i).toString()));
            i++;
        }

        Column finalCol = new Column(this.name + "/" + another.getName(), finalList, "d");
        return finalCol;
    }

    //REQUIRES: two columns have unique elements
    //EFFECTS: return True if elements of two Columns are the same
    public Boolean checkEqualsUnique(Column another) {
//        if (!checkUnique() || !another.checkUnique()) {
//            return false;
//        }
        Set<Object> set = new TreeSet<>(another.getColAsList());
        for (Object o:column) {
            if (set.add(o)) {
                return false;
            }
        }
        return true;

    }

    //EFFECTS: return name of column
    public String getName() {
        return name;
    }

    //EFFECTS: return initial of datatype of column
    public String getType() {
        return type;
    }

    //EFFECTS: return size of column
    public Integer getSize() {
        return size;
    }

    //REQUIRES: 0 < index < this.size()
    //EFFECTS: return column.get(index)
    public Object get(int index) {
        Object o = column.get(index);
        return o;
    }

    //REQUIRES: Object o exist in this.column
    //EFFECTS: return the index of o (first occurrance)
    public Integer getIndexByObject(Object o) {
        return column.indexOf(o);
    }

    //EFFECTS: return  this.column
    private List<Object> getColAsList() {
        List<Object> l = new LinkedList<>();
        l.addAll(column);
        return l;
    }


    //EFFECTS: return True if actual type of elements of column is Integer or Double,
    //          return False otherwise.
    public Boolean checkIfArithmetic() {
        return ((type.equals("i") || type.equals("d")));
    }

    //REQUIRES: Column another has same exact and unique elements as this.column
    //EFFECTS: return a list of positions in another w.r.t. this.column
    public List<Integer> checkPositionInAnother(Column another) {
        List<Integer> positionInAnother = new LinkedList<>();
        for (Object o:getColAsList()) {
            positionInAnother.add(another.getColAsList().indexOf(o));
        }
        return positionInAnother;
    }

    //REQUIRES: Column another has same length as this.column
    //MODIFIES: this
    //EFFECTS: add elements of another according to positionInAnother
    public void insertElementInOrder(Column another, List<Integer> positionInAnother) {
        for (Integer i:positionInAnother) {
            addElement(another.get(i));
        }
    }

    //EFFECTS: return true if column contains o, else return false
    public boolean contains(Object o) {
        return getColAsList().contains(o);
    }


    // EFFECTS: returns Column as a JSON object
    // reference: JsonSerializationDemo
    //            https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("column", getColAsList());
        json.put("name", getName());
        json.put("type", getType());

        //JSONObject namesInJson = new JSONObject();
        json.put("size", getSize());
        return json;
    }
}
