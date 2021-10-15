package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ColumnTest {
    Column colI, colD, colS;
    List<Object> listI, listD, listS;
    @BeforeEach
    void ini(){
        listI = new LinkedList<>();
        listI.add(-7);
        listI.add(0);
        listI.add(9);
        listI.add(79);
        listD = new LinkedList<>();
        listD.add(0);
        listD.add(-9.7861);
        listD.add(1.234);
        listD.add(279.436);
        listS = new LinkedList<>();
        listS.add("A");
        listS.add("b");
        listS.add("Cc");
        listS.add("Dsef");
        colI = new Column("colI", listI, "i");
        colD = new Column("colD", listD, "d");
        colS = new Column("colS", listS, "s");

        colI.specifyType("i");
        colD.specifyType("d");
        colS.specifyType("s");
    }

    @Test
    void checkUnique() {
        assertTrue(colS.checkUnique());

        colS.addElement("b");
        colS.checkUnique();
        assertFalse(colS.checkUnique());

        List<Object> list = new LinkedList<>();
        list.add(7);
        list.add(9);
        list.add(7);
        list.add(-9);
        Column col = new Column("test", list, "i");
        assertFalse(col.checkUnique());
    }

    //specify
    @Test
    void specifyTypeTest() {
        colI.specifyType("i");
        assertEquals(colI.getType(), "i");
        colI.specifyType("o");
        assertEquals(colI.getType(), "o");
        colI.specifyType("s");
        assertEquals(colI.getType(), "s");
        colI.specifyType("d");
        assertEquals(colI.getType(), "d");
        colI.specifyType("i");
        assertEquals(colI.getType(), "i");
        colD.specifyType("i");
        assertEquals(colD.getType(), "i");
        colS.specifyType("s");
        assertEquals(colS.getType(), "s");
    }

    //name
    @Test
    void renameColTest() {
        colI.renameCol("hi");
        assertEquals(colI.getName(), "hi");
    }

    //list
    @Test
    void listAsColTest(){
        Column colI2 = new Column("colI2", listI, "i");
        //colI2.listAsCol(listI);
        assertEquals(colI2.getSize(), listI.size());
    }

    @Test
    void appendColTest(){
        List<Object> list1 = new LinkedList<>();
        list1.add(9);
        list1.add(-84);
        int oldSize = colI.getSize();

        Column col1 = new Column("col1", list1, "i");
        //col1.listAsCol(list1);
        colI.appendCol(col1);
        int newSize = oldSize + list1.size();
        assertEquals(colI.getSize(), newSize);

        Column col2 = new Column("col2", new LinkedList<>(), "i");
        col2.appendCol(col1);
        assertEquals(col2.getSize(), list1.size());
    }

    @Test
    void removeTest() {
        colS.remove(0);
        assertEquals(colS.getSize(), 3);
        assertEquals(colS.get(0), "b");

        colI.remove(colI.getSize()-1);
        assertEquals(colI.getSize(), 3);
        assertEquals(colI.get(colI.getSize()-1), 9);
    }
    @Test
    void addElementTest() {
        colS.addElement("5");
        assertEquals(colS.getSize(), 5);
        assertEquals(colS.get(4), "5");

        colI.addElement(9249);
        assertEquals(colI.getSize(), 5);
        assertEquals(colI.get(colI.getSize()-1), 9249);

    }

    //add
    @Test
    void addTestSame() {
        List<Object> list2 = new LinkedList<>();
        list2.add(-1); list2.add(-39); list2.add(-7); list2.add(97);

        Column col2 = new Column("col2", list2, "i");
//        col2.listAsCol(list2);
//        col2.specifyType("i");
//        colI.specifyType("i");

        Column sum = col2.add(colI);
        assertEquals(sum.get(0), -7+-1);
        assertEquals(sum.get(1), 0-39);
        assertEquals(sum.get(2), 9-7);
        assertEquals(sum.get(3), 79+97);
        assertEquals( sum.getType(), "i");
        assertEquals( sum.getName(), "col2+colI");

        List<Object> list3 = new LinkedList<>();
        list3.add(-1.0); list3.add(-39.8); list3.add(-7.14); list3.add(97.5);

        Column col3 = new Column("col3", list3, "d");
        sum = col3.add(colD);
        assertTrue( checkPrecision((double) sum.get(0), (-1.0+0)  ));
        assertTrue( checkPrecision((double) sum.get(1), (-39.8+-9.7861) ));
        assertTrue( checkPrecision((double) sum.get(2), (-7.14+1.234)  ));
        assertTrue( checkPrecision((double) sum.get(3), (97.5+279.436) ));
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "col3+colD");
    }

    @Test
    void addTestDiff() {
        List<Object> list2 = new LinkedList<>();
        list2.add(-1.0); list2.add(-39.8); list2.add(-7.14); list2.add(97.5);

        Column col2 = new Column("col2", list2, "d");
        //col2.listAsCol(list2);

        Column sum = col2.add(colI);
        assertTrue( checkPrecision((double) sum.get(0), (-1.0+-7)  ));
        assertTrue( checkPrecision((double) sum.get(1), (-39.8+0) ));
        assertTrue( checkPrecision((double) sum.get(2), (-7.14+9)  ));
        assertTrue( checkPrecision((double) sum.get(3), (97.5+79) ));
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "col2+colI");
    }

    //minus
    @Test
    void minusTestSame() {
        List<Object> list2 = new LinkedList<>();
        list2.add(-1); list2.add(-39); list2.add(-7); list2.add(97);


        Column col2 = new Column("col2", list2, "i");

        Column sum = col2.minus(colI);
        assertEquals(sum.get(0), -1+7);
        assertEquals(sum.get(1), -39-0);
        assertEquals(sum.get(2), -7-9);
        assertEquals(sum.get(3), 97-79);
        assertEquals( sum.getType(), "i");
        assertEquals( sum.getName(), "col2-colI");

        List<Object> list3 = new LinkedList<>();
        list3.add(-1.0); list3.add(-39.8); list3.add(-7.14); list3.add(97.5);

        Column col3 = new Column("col3", list3, "d");

        sum = col3.minus(colD);
        assertTrue( checkPrecision((double) sum.get(0), (-1.0-0)  ));
        assertTrue( checkPrecision((double) sum.get(1), (-39.8+9.7861) ));
        assertTrue( checkPrecision((double) sum.get(2), (-7.14-1.234)  ));
        assertTrue( checkPrecision((double) sum.get(3), (97.5-279.436) ));
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "col3-colD");
    }

    @Test
    void minusTestDiff() {
        List<Object> list2 = new LinkedList<>();
        list2.add(-1.0); list2.add(-39.8); list2.add(-7.14); list2.add(97.5);

        Column col2 = new Column("col2", list2, "d");

        Column sum = col2.minus(colI);
        assertTrue( checkPrecision((double) sum.get(0), (-1.0+7)  ));
        assertTrue( checkPrecision((double) sum.get(1), (-39.8-0) ));
        assertTrue( checkPrecision((double) sum.get(2), (-7.14-9)  ));
        assertTrue( checkPrecision((double) sum.get(3), (97.5-79) ));
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "col2-colI");
    }

    //multiply
    @Test
    void multiplyTestSame() {
        List<Object> list2 = new LinkedList<>();
        list2.add(-1); list2.add(-39); list2.add(-7); list2.add(97);

        Column col2 = new Column("col2", list2, "i");

        Column sum = col2.multiply(colI);
        assertEquals(sum.get(0), -1*-7);
        assertEquals(sum.get(1), -39*0);
        assertEquals(sum.get(2), -7*9);
        assertEquals(sum.get(3), 97*79);
        assertEquals( sum.getType(), "i");
        assertEquals( sum.getName(), "col2*colI");

        List<Object> list3 = new LinkedList<>();
        list3.add(-1.0); list3.add(-39.8); list3.add(-7.14); list3.add(97.5);

        Column col3 = new Column("col3", list3, "d");

        sum = col3.multiply(colD);
        assertTrue( checkPrecision((double) sum.get(1), (-39.8*-9.7861)  ));
        assertTrue( checkPrecision((double) sum.get(2), (-7.14*1.234)   ));
        assertTrue( checkPrecision((double) sum.get(3), (97.5*279.436) ));
        assertTrue(((-1.0*0)< 0.00001) && ((double) sum.get(0) < 0.00001));
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "col3*colD");
    }

    @Test
    void multiplyTestDiff() {
        List<Object> list2 = new LinkedList<>();
        list2.add(-1.0); list2.add(-39.8); list2.add(-7.14); list2.add(97.5);

        Column col2 = new Column("col2", list2, "d");

        Column sum = col2.multiply(colI);

        assertTrue( checkPrecision((double) sum.get(0), (-1.0*-7) ));
        assertTrue( checkPrecision((double) sum.get(2), (-7.14*9)  ));
        assertTrue( checkPrecision((double) sum.get(3), (97.5*79)  ));
        assertTrue(((-39.8*0)< 0.00001) && (double) sum.get(1) < 0.00001);
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "col2*colI");
    }

    boolean checkPrecision(double a, double b) {
        return (Math.abs(a-b) / a < 0.00001);
    }

    //divide
    @Test
    void divideDoubleTestSame() {
        List<Object> list2 = new LinkedList<>();
        list2.add(-1); list2.add(-39); list2.add(-7); list2.add(97);

        Column col2 = new Column("col2", list2, "i");
        //colI.specifyType("i");

        Column sum = col2.divideDouble(colI);
        assertTrue( checkPrecision((double) sum.get(0), (double) -1/-7) );
        assertTrue( checkPrecision((double) sum.get(2), (double) -7/9) );
        assertTrue( checkPrecision((double) sum.get(3), (double) 97/79) );
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "col2/colI");

        List<Object> list3 = new LinkedList<>();
        list3.add(-1.0); list3.add(-39.8); list3.add(-7.14); list3.add(97.5);

        Column col3 = new Column("col3", list3, "d");

        sum = col3.divideDouble(colD);
        assertTrue( checkPrecision((double) sum.get(1), (-39.8/-9.7861) ));
        assertTrue( checkPrecision((double) sum.get(2), (-7.14/1.234) ));
        assertTrue( checkPrecision((double) sum.get(3), (97.5/279.436) ));
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "col3/colD");
    }

    @Test
    void divideDoubleTestDiff() {
        List<Object> list2 = new LinkedList<>();
        list2.add(-1.0); list2.add(-39.8); list2.add(-7.14); list2.add(97.5);

        Column col2 = new Column("col2", list2, "d");

        Column sum = col2.divideDouble(colI);
        assertTrue( checkPrecision((double) sum.get(0), (-1.0/-7) ));
        assertTrue( checkPrecision((double) sum.get(2), (-7.14/9)  ));
        assertTrue( checkPrecision((double) sum.get(3), (97.5/79)  ));
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "col2/colI");

        sum = colI.divideDouble(col2);
        assertTrue( checkPrecision((double) sum.get(0), 1/(-1.0/-7) ));
        assertTrue( checkPrecision((double) sum.get(2), 1/(-7.14/9)  ));
        assertTrue( checkPrecision((double) sum.get(3), 1/(97.5/79)  ));
        assertEquals( sum.getType(), "d");
        assertEquals( sum.getName(), "colI/col2");
    }

    @Test
    void checkEqualUnique() {
        List<Object> list = new LinkedList<>();
        list.add("b");
        list.add("Cc");
        list.add("A");
        list.add("Dsef");
        Column col2 = new Column();
        col2.listAsCol(list);
        col2.specifyType("s");
        col2.renameCol("col2");

        assertTrue(col2.checkEqualsUnique(colS));

        col2.addElement("lmao");
        assertFalse(col2.checkEqualsUnique(colS));
    }


    //getName
    @Test
    void getNameTest() {
        colI.renameCol("abcdfnde");
        assertTrue(colI.getName() == "abcdfnde");
        assertTrue(colD.getName() == "colD");
    }

    @Test
    void getTypeTest() {
        assertTrue(colI.getType() == "i");
        assertTrue(colD.getType() == "d");
        assertTrue(colS.getType() == "s");
        Column col1 = new Column("col1", new ArrayList<>(), "o");
        assertTrue(col1.getType() == "o");
    }

    @Test
    void getIndexByObjectTest() {
        assertEquals(colS.getIndexByObject(colS.get(2)), 2);
        colS.addElement("Cc");
        assertEquals(colS.getIndexByObject(colS.get(2)), 2);
        colS.remove(2);
        assertEquals(colS.getIndexByObject("Cc"), 3);
    }

    @Test
    void checkIfArithmeticTest() {
        assertTrue(colI.checkIfArithmetic());
        assertTrue(colD.checkIfArithmetic());
        assertFalse(colS.checkIfArithmetic());
        Column col1 = new Column("col1", new ArrayList<>(), "o");
        assertFalse(col1.checkIfArithmetic());
    }

    @Test
    void checkPositionInAnother() {
        List<Object> list1 = new LinkedList<>();
        list1.add("Sam");
        list1.add("Ada");
        list1.add("Mada");
        list1.add("Joshua");
        Column name1 = new Column("name1", list1, "s");

        List<Object> list2 = new LinkedList<>();
        list2.add("Mada");
        list2.add("Ada");
        list2.add("Joshua");
        list2.add("Sam");
        Column name2 = new Column("name2", list2, "s");

        List<Integer> position = name1.checkPositionInAnother(name2);
        assertEquals(position.get(0), 3);
        assertEquals(position.get(1), 1);
        assertEquals(position.get(2), 0);
        assertEquals(position.get(3), 2);

    }

    @Test
    void insertElementInOrderTest() {
        List<Object> list1 = new LinkedList<>();
        list1.add("Sam");
        list1.add("Ada");
        list1.add("Mada");
        list1.add("Joshua");
        Column name1 = new Column("name1", list1, "s");

        List<Object> list2 = new LinkedList<>();
        list2.add("Mada");
        list2.add("Ada");
        list2.add("Joshua");
        list2.add("Sam");
        Column name2 = new Column("name2", list2, "s");

        List<Integer> position = name1.checkPositionInAnother(name2);

        List<Object> list3 = new LinkedList<>();
        list3.add("17");
        list3.add("21");
        list3.add("-27");
        list3.add("53");
        Column colToInsert = new Column("colToInsert", list3, "i");

        Column col = new Column();
        col.insertElementInOrder(colToInsert, position);
        assertEquals(col.get(0), 53);
        assertEquals(col.get(1), 21);
        assertEquals(col.get(2), 17);
        assertEquals(col.get(3), -27);

    }

    @Test
    void containsTest() {
        assertTrue(colI.contains(-7));
        assertFalse(colS.contains("a"));
    }




}
