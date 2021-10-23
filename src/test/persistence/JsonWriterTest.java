package persistence;

import model.Column;
import model.Data;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonWriterTest {
    Data d;


    @Test
    void testGeneralData() {
        d = new Data();
        d.importFile("testing.csv");
        d.specifyColType(d.getCol(0), "s");
        d.specifyColType(d.getCol(1), "o");
        d.specifyColType(d.getCol(2), "i");
        d.specifyColType(d.getCol(3), "d");
        d.setIndex(d.getCol(0));

        try {
            JsonWriter writer = new JsonWriter("./data/testTestingcsv.json");
            writer.open();
            writer.write(d);
            writer.close();

            JsonReader reader = new JsonReader("./data/testTestingcsv.json");
            d = reader.read();
            assertEquals(0,d.getIndex());

            for (int i=0; i<d.getNumOfCol(); i++) {
                Column col = d.getCol(i);
                assertEquals(col.getName(), d.getNames().get(i));
                assertEquals(col.getSize(), d.getNumOfRow());
                //checkCol()/?
            }
//            checkThingy("saw", Category.METALWORK, thingies.get(0));
//            checkThingy("needle", Category.STITCHING, thingies.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
