package model.utils.output;

import model.Theater;
import model.utils.temp.InputData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonExporter extends Exporter{

    @Override
    public void exportFile(String filePath, InputData storage) {
        JSONArray theaters = formatTheaters(storage);
        JSONArray rooms = formatRooms(storage);
    }

    private JSONArray formatTheaters(InputData storage) {
        JSONArray theaterList = new JSONArray();
        for (Theater theater : storage.getTheaters()) {
            JSONObject theaterDetails = new JSONObject();
            theaterDetails.put("id", theater.getId());
            theaterDetails.put("name", theater.getName());
            theaterList.add(theaterDetails);
        }
        return theaterList;
    }

    private JSONArray formatRooms(InputData storage) {
        JSONArray roomList = new JSONArray();
        return roomList;
    }
}
