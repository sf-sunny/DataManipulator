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

// Use JsonWriter to write a file and then check with reading the file by JsonReader
// reference: JsonSerializationDemo
//            https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest {
    Data d;

    @Test
    void testWriterInvalidFile() {
        try {
            Data d1 = new Data();
            JsonWriter writer = new JsonWriter("./data/An\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Data d1 = new Data();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyData.json");
            writer.open();
            writer.write(d1);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyData.json");
            Data d2 = reader.read();

            assertEquals(d2.getData().size(), 0);
            assertEquals(d2.getNames().size(), 0);
            assertEquals(d2.getNumOfCol(), 0);
            assertEquals(d2.getNumOfRow(), 0);
            assertEquals(d2.getIndex(), -1);

        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }

    }

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
            JsonWriter writer = new JsonWriter("./data/testWriterTestingcsv.json");
            writer.open();
            writer.write(d);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterTestingcsv.json");
            Data d1 = reader.read();
            assertEquals(0,d1.getIndex());

            for (int i=0; i<d1.getNumOfCol(); i++) {
                Column col = d1.getCol(i);
                assertEquals(col.getName(), d.getNames().get(i));
                assertEquals(col.getSize(), d.getNumOfRow());
                assertEquals(d1.getNumOfCol(),4);
            }

            assertEquals(d.getInitialNumOfCol(), d1.getInitialNumOfCol());
            assertEquals(4, d1.getInitialNumOfCol());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }



}
