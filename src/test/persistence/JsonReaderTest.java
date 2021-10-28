package persistence;

import model.Data;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// reference: JsonSerializationDemo
//            https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Data d = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }
    @Test
    void testReaderEmptyData() {
        try {
            JsonReader reader = new JsonReader("./data/testReaderEmptyData.json");
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
    void testReaderGeneralData() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralData.json");
        try {
            Data d2 = reader.read();
            assertEquals(d2.getData().size(), 4);
            assertEquals(d2.getNames().size(), 4);
            assertEquals(d2.getNumOfCol(), 4);
            assertEquals(d2.getNumOfRow(), 3);
            assertEquals(d2.getIndex(), 1);

            assertEquals(d2.getCol(0).get(0), "Wai");
            assertEquals(d2.getCol(1).get(1), "a8a0o");
            assertEquals(d2.getCol(2).get(2), 21);
            assertEquals(d2.getCol(3).get(1), 163.1);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }

    }

}
