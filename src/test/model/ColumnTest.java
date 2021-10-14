package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        colS = new Column("colS", listI, "s");

        colI.specifyType("i");
        colD.specifyType("d");
        colS.specifyType("s");
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
    void checkIfArithmeticTest() {
        assertTrue(colI.checkIfArithmetic());
        assertTrue(colD.checkIfArithmetic());
        assertFalse(colS.checkIfArithmetic());
        Column col1 = new Column("col1", new ArrayList<>(), "o");
        assertFalse(col1.checkIfArithmetic());
    }
}
