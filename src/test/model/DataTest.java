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

    //concatenate
    @Test
    void concatTest() {
        Data data2 = new Data(d);


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


}