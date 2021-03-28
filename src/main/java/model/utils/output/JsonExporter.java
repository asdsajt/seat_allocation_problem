package model.utils.output;

import model.Room;
import model.Seat;
import model.Theater;
import model.utils.interfaces.IStorage;
import model.utils.temp.InputData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JsonExporter extends Exporter{

    private final IStorage storage;

    public JsonExporter(IStorage storage) {
        this.storage = storage;
    }

    @Override
    public void exportFile(String filePath) {
        JSONObject fullJson = new JSONObject();
        fullJson.put("theaters", formatTheaters());
        fullJson.put("rooms", formatRooms());
        writeJson(fullJson, filePath);
    }

    private JSONArray formatTheaters() {
        JSONArray theaterList = new JSONArray();
        for (Theater theater : storage.getTheaters()) {
            JSONObject theaterDetails = new JSONObject();
            theaterDetails.put("id", theater.getId());
            theaterDetails.put("name", theater.getName());
            theaterList.add(theaterDetails);
        }
        return theaterList;
    }

    private JSONArray formatRooms() {
        JSONArray roomList = new JSONArray();
        for (Room room : storage.getRooms()) {
            JSONObject roomDetails = new JSONObject();
            roomDetails.put("id", room.getId());
            roomDetails.put("theater_id", room.getTheaterId());
            roomDetails.put("name", room.getName());
            roomDetails.put("column_num", room.getColumnNum());
            roomDetails.put("row_num", room.getRowNum());
            JSONObject rows = formatSeats(room);
            roomDetails.put("rows", rows);
            roomList.add(roomDetails);
        }
        return roomList;
    }

    private JSONObject formatSeats(Room room) {
        JSONObject rowsObject = new JSONObject();
        for (int i = 0; i < room.getRowNum(); i++) {
            Seat[] row = room.getRow(i);
            JSONArray rowDetails = new JSONArray();
            for (Seat seat : row) {
                JSONObject seatDetails = new JSONObject();
                seatDetails.put("status", seat.getStatus().ordinal());
                seatDetails.put("orderId", seat.getOrderId());
                rowDetails.add(seatDetails);
            }
            rowsObject.put("" + i, rowDetails);
        }
        return rowsObject;
    }

    private void writeJson(JSONObject fullJson, String filePath) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(fullJson.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Output operation interrupted");  // TODO: Move it to GUI
            e.printStackTrace();
        }
    }
}
