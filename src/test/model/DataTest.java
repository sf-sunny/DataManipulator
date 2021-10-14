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
    }

    //importFile
    @Test
    void importFileTest() {
        assertTrue(d.importFile("testing.csv"));
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

        assertEquals(d.getCol(5).get(0), 160+160.0);
        assertEquals(d.getCol(5).get(3), -169.9-169.9);
    }

    @Test
    void arithmeticsTestMinus() {

    }

    @Test
    void arithmeticsTestMulti() {

    }

    @Test
    void arithmeticsTestDivide() {

    }

    //concatenate
    @Test
    void concatTest() {
        Data data2 = new Data(d);


    }

    //visualize
    @Test
    void visualizeTestBarSingle() {

    }

    @Test
    void visualizeTestBarMulti() {

    }

    @Test
    void visualizeTestLineSingle() {

    }

    @Test
    void visualizeTestLineMulti() {

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
    void getNumberOfColTest() {
        assertEquals(d.getNumOfCol(),5);
    }

    @Test
    void getNumberOfRowTest() {
        assertEquals(d.getNumOfCol(),4);
    }
}