package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataTest {
    Data d;
    @BeforeEach
    void init(){
        d = new Data();
        d.importFile("testing.csv");
        d.specifyColType(d.getCol(0), "s");
        d.specifyColType(d.getCol(1), "o");
        d.specifyColType(d.getCol(2), "i");
        d.specifyColType(d.getCol(3), "d");
    }



    //importFile
    @Test
    void importFileTest() {
        assertTrue(d.importFile("testing.csv"));
        assertFalse(d.importFile("testing___.csv"));
    }

    @Test
    void setIndexTest() {
        d.getCol("Name");
        assertTrue(d.setIndex(d.getCol("Name")));
        assertTrue(d.setIndex(d.getCol("CSID")));
        assertTrue(d.setIndex(d.getCol("Age")));
        assertTrue(d.setIndex(d.getCol("Height")));

        List<Object> list = new LinkedList<>();
        list.add("Wai");
        list.add("9ahah");
        list.add("18");
        list.add("197.0");
        d.addRow(list);
        assertFalse(d.setIndex(d.getCol("Name")));
        assertTrue(d.setIndex(d.getCol("CSID")));
    }

    //specifyColType
    @Test
    void specifyColTypeTestSingle() {
        d.specifyColType(d.getCol(0), "s");
        d.specifyColType(d.getCol(1), "o");
        d.specifyColType(d.getCol(2), "i");
        d.specifyColType(d.getCol(3), "d");
        assertEquals(d.getColType(0), "s");
        assertEquals(d.getColType(1), "o");
        assertEquals(d.getColType(2), "i");
        assertEquals(d.getColType(3), "d");

        d.specifyColType(d.getCol(2), "d");
        assertEquals(d.getCol("Age").get(0), 17.0);
    }

    @Test
    void removeRowTest() {
        d.removeRow(3);
        assertEquals(d.getNumOfRow(), 4);
        assertEquals(d.getCol("Name").get(3), "Wai");
        assertEquals(d.getCol("Name").get(2), "Sunny");

        d.setIndex(d.getCol("Name"));
        d.removeRow("Sunny");
        assertEquals(d.getCol("CSID").get(2), "haha9");
        assertEquals(d.getCol("CSID").get(0), "s1s2t");
        assertEquals(d.getNumOfRow(), 3);
    }

    //arithmetics

    @Test
    void arithmeticsTestInvalidOp() {
        d.arithmetics(d.getCol(2),"++", d.getCol(2));
        d.arithmetics(d.getCol(2),"+-", d.getCol(3));

        assertEquals(d.getNumOfCol(), 4);
        assertEquals(d.getNumOfRow(), 5);
    }
    @Test
    void arithmeticsTestAdd() {
        d.arithmetics(d.getCol(2),"+", d.getCol(2));
        d.arithmetics(d.getCol(2),"+", d.getCol(3));
        d.arithmetics(d.getCol(3),"+", d.getCol(3));

        assertEquals(d.getCol(4).get(0), 17+17);
        assertEquals(d.getCol(4).get(2), 21+21);

        assertEquals(d.getCol(5).get(1), 20+163.1);
        assertEquals(d.getCol(5).get(3), 27-169.9);

        assertEquals(d.getCol(6).get(0), 160+160.0);
        assertEquals(d.getCol(6).get(3), -169.9-169.9);
    }

    @Test
    void arithmeticsTestMinus() {
        d.arithmetics(d.getCol(2),"-", d.getCol(2));
        d.arithmetics(d.getCol(2),"-", d.getCol(3));
        d.arithmetics(d.getCol(3),"-", d.getCol(3));

        assertEquals(d.getCol(4).get(0), 17-17);
        assertEquals(d.getCol(4).get(2), 21-21);

        assertEquals(d.getCol(5).get(1), 20-163.1);
        assertEquals(d.getCol(5).get(3), 27+169.9);

        assertEquals(d.getCol(6).get(0), 160-160.0);
        assertEquals(d.getCol(6).get(3), -169.9+169.9);
    }

    @Test
    void arithmeticsTestMulti() {
        d.arithmetics(d.getCol(2),"*", d.getCol(2));
        d.arithmetics(d.getCol(2),"*", d.getCol(3));
        d.arithmetics(d.getCol(3),"*", d.getCol(3));

        assertEquals(d.getCol(4).get(0), 17*17);
        assertEquals(d.getCol(4).get(2), 21*21);

        assertEquals(d.getCol(5).get(1), 20*163.1);
        assertEquals(d.getCol(5).get(3), 27*-169.9);

        assertEquals(d.getCol(6).get(0), 160*160.0);
        assertEquals(d.getCol(6).get(3), -169.9*-169.9);
    }

    @Test
    void arithmeticsTestDivide() {
        d.arithmetics(d.getCol(2),"/", d.getCol(2));
        d.arithmetics(d.getCol(2),"/", d.getCol(3));
        d.arithmetics(d.getCol(3),"/", d.getCol(3));
        d.arithmetics(d.getCol("Height"),"/", d.getCol("Age"));

        Column col = d.getCol("Age/Age");
        col.getName();
        assertEquals(d.getCol("Age/Age").get(0), 17/17.0);
        assertEquals(d.getCol("Age/Age").get(2), 21/21.0);

        assertEquals(d.getCol("Age/Height").get(1), 20/163.1);
        assertEquals(d.getCol("Age/Height").get(3), 27/-169.9);

        assertEquals(d.getCol("Height/Height").get(0), 160/160.0);
        assertEquals(d.getCol("Height/Height").get(3), -169.9/-169.9);

        assertEquals(d.getCol("Height/Age").get(0), 160.0/17);
        assertEquals(d.getCol("Height/Age").get(4), 162.3/18);

    }

    @Test
    void checkIndexPositionInAnotherTest() {
        Data data2 = new Data();
        data2.importFile("testConcatName.csv");

        //setIndex
        data2.setIndex(data2.getCol("Name"));
        d.setIndex(d.getCol("Name"));

        List<Integer> pos = d.checkIndexPositionInAnother(data2);
        assertEquals(pos.get(0),3);
        assertEquals(pos.get(1),2);
        assertEquals(pos.get(2),0);
        assertEquals(pos.get(3),1);
        assertEquals(pos.get(4),4);

    }
    //concatenate
    @Test
    void concatTestOnName() {
        Data data2 = new Data();
        data2.importFile("testConcatName.csv");

        //setIndex
        data2.setIndex(data2.getCol("Name"));
        d.setIndex(d.getCol("Name"));

        d.concatenate(data2);
        assertEquals(d.getNumOfRow(), 5);
        assertEquals(d.getNumOfCol(), 6);

        //Age update on Jacky
        d.specifyColType(d.getCol("Age"), "i");
        assertEquals(d.getCol("Age").get(3), 29);
        assertEquals(d.getCol("Age").get(0), 17);

        d.specifyColType(d.getCol("Score"), "i");
        assertEquals(d.getCol("Score").get(1), 57);

        assertEquals(d.getCol("Sex").get(4), "X");
    }

    @Test
    void concatTestOnDiffName() {
        Data data2 = new Data();
        data2.importFile("testConcatDiffName.csv");

        //setIndex
        data2.setIndex(data2.getCol("Name"));
        d.setIndex(d.getCol("Name"));

        assertFalse(d.concatenate(data2));
    }
    @Test
    void concatTestSame() {
        Data data = new Data();
        data.copyFromData(d);

        data.setIndex(data.getCol("Name"));
        d.setIndex(d.getCol("Name"));

        data.concatenate(d);
        assertEquals(data.getNumOfRow(), 5);
        assertEquals(data.getNumOfCol(), 4);
    }



    //addCol
    @Test
    void addColTest() {
        List<Object> list = new LinkedList<>();
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(-9);
        Column col = new Column("test", list, "i");

        d.addCol(col);

        assertEquals(d.getNumOfCol(), 5);
        assertEquals(d.getCol(4).get(0), 1);
        assertEquals(d.getCol(4).get(3), -9);
    }

    @Test
    void removeColTest() {
        d.setIndex(d.getCol("CSID"));
        assertEquals(d.getIndex(), 1);
        assertEquals(d.getNumOfCol(), 4);

        d.removeCol(3);
        assertEquals(d.getIndex(), 1);
        assertEquals(d.getNumOfCol(), 3);

        d.removeCol(0);
        assertEquals(d.getIndex(), 0);
        assertEquals(d.getNumOfCol(), 2);

        d.removeCol("CSID");
        assertEquals(d.getIndex(), -1);
        assertEquals(d.getNumOfCol(), 1);
    }


    //getCol
    @Test
    void getColTest() {
        Column col = d.getCol(0);
        assertEquals(col.get(0),"Sam");
        assertEquals(col.get(2),"Sunny");
        assertEquals(col.getSize(),5);

        col = d.getCol(2);
        assertEquals(col.get(1),20);
        assertEquals(col.get(3),27);
        assertEquals(col.getSize(),5);
    }

    @Test
    void getColTypeTest() {
        assertEquals(d.getColType(0), "s");
        assertEquals(d.getColType("Name"), "s");
        assertEquals(d.getColType(3), "d");
        assertEquals(d.getColType("Height"), "d");
    }


    @Test
    void getNumberOfColTest() {
        assertEquals(d.getNumOfCol(),4);
    }

    @Test
    void getNumberOfRowTest() {
        assertEquals(d.getNumOfCol(),4);
    }

    @Test
    void isEmptyTest() {
        assertFalse(d.isEmpty());
        Data d1 = new Data();
        assertTrue(d1.isEmpty());
        Column col = new Column();

        d1.copyFromData(d);
        d1.removeRow(0);
        d1.removeRow(0);
        d1.removeRow(0);
        d1.removeRow(0);
        d1.removeRow(0);
        assertEquals(d1.getNumOfCol(), 4);
        assertEquals(d1.getNumOfRow(), 0);
        assertFalse(d1.isEmpty());

        d1.copyFromData(d);
        d1.removeCol(0);
        d1.removeCol(0);
        d1.removeCol(0);
        d1.removeCol(0);
        assertEquals(d1.getNumOfCol(), 0);
        assertEquals(d1.getNumOfRow(), 0);
        assertTrue(d1.isEmpty());
    }

    @Test
    void checkIfAnyArithmeticsTest() {
        assertTrue(d.checkIfAnyArithmetics());

        d.specifyColType(d.getCol(0), "s");
        d.specifyColType(d.getCol(1), "o");
        d.specifyColType(d.getCol(2), "s");
        d.specifyColType(d.getCol(3), "s");

        assertFalse(d.checkIfAnyArithmetics());
    }


}