package persistence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import model.*;

// Represents a reader that reads Data from JSON data stored in file
// reference: JsonSerializationDemo
//            https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Data from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Data read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        //EventLog.getInstance().logEvent(new Event("File read: " + source));
        return parseData(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Data from JSON object and returns it
    private Data parseData(JSONObject jsonObject) {
        Data d = new Data();
        addColumns(d, jsonObject);
        int index = jsonObject.getInt("index");
        if (index != -1) {
            d.setIndex(d.getCol(index));
        }
        d.setInitialNumOfCol(jsonObject.getInt("initialNumOfCol"));
        return d;
    }

    // MODIFIES: d
    // EFFECTS: parses Columns from JSON object and adds them to Data d
    private void addColumns(Data d, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (Object json : jsonArray) {
            JSONObject nextCol = (JSONObject) json;
            addColumn(d, nextCol);
        }
    }

    // MODIFIES: d
    // EFFECTS: parses Column from JSON object and adds it to d
    private void addColumn(Data d, JSONObject jsonObject) {
        String name = jsonObject.getString("name");

        List<Object> list = new LinkedList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("column");
        for (Object json : jsonArray) {
            list.add(json);
        }

        String type = jsonObject.getString("type");

        Column col = new Column(name, list, type);
        d.addCol(col);
    }
}
