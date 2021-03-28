package database;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Room;
import model.Theater;
import model.utils.enums.SeatStatus;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import java.util.ArrayList;

public class DatabaseHandler {

    private boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    private MongoClientURI uri = new MongoClientURI("mongodb+srv://dev_andras:VaYghVLpk9claZvL@cluster0.w1odn.mongodb.net/Theater?retryWrites=true&w=majority");

    public Room testRoom(){
        Room room = new Room("RO-123", "TH-1", 4, 5);

        room.getRows()[0][2].setStatus(SeatStatus.Taken); room.getRows()[0][4].setStatus(SeatStatus.Taken);
        room.getRows()[1][2].setStatus(SeatStatus.Taken); room.getRows()[1][4].setStatus(SeatStatus.Taken);
        room.getRows()[2][2].setStatus(SeatStatus.Taken); room.getRows()[2][4].setStatus(SeatStatus.Taken);
        room.getRows()[3][2].setStatus(SeatStatus.Taken); room.getRows()[3][4].setStatus(SeatStatus.Taken);

        return room;
    }

    public void updateRoom(Room room){
        BasicDBObject query = new BasicDBObject();
        query.put(findRoomKey(room), convertOriginalToObject(room.getId()));

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put(findRoomKey(room), convertRoomToObject(room));

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);

        try(MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Room");
            collection.updateOne(query, updateObject);
        }
        catch (Exception e){
            throw e;
        }
    }

    public void saveNewRoom(Room room){
        BasicDBObject query = new BasicDBObject();
        query.put(getNewRoomIndex(), convertRoomToObject(room));

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", query);

        try(MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Room");
            collection.updateOne(new Document(), updateObject);
        }
        catch (Exception e){
            throw e;
        }
    }

    private String getNewRoomIndex(){
        int big = 0;

        try(MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Room");
            Document querry = new Document("_id", new ObjectId("605e1dbfc3e6d4caaf7cb196"));
            Document result = collection.find(querry).iterator().next();

            JSONObject jo = new JSONObject(result.toJson());
            for (String element: jo.keySet()) {
                if (isInteger(element, 10)) {
                    if (big < Integer.parseInt(element)){
                        big = Integer.parseInt(element);
                    }
                }
            }
        }

        return String.valueOf(big+1);
    }

    private String getNewTheaterIndex(){
        int big = 0;

        try(MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Theater");
            Document querry = new Document("_id", new ObjectId("605e1e30c3e6d4caaf7cb198"));
            Document result = collection.find(querry).iterator().next();

            JSONObject jo = new JSONObject(result.toJson());
            for (String element: jo.keySet()) {
                if (isInteger(element, 10)) {
                    if (big < Integer.parseInt(element)){
                        big = Integer.parseInt(element);
                    }
                }
            }
        }

        return String.valueOf(big+1);
    }

    private String findRoomKey(Room room){
        try(MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Room");
            Document querry = new Document("_id", new ObjectId("605e1dbfc3e6d4caaf7cb196"));
            Document result = collection.find(querry).iterator().next();

            JSONObject jo = new JSONObject(result.toJson());
            for (String element: jo.keySet()) {
                if (isInteger(element, 10)) {
                    if(jo.getJSONObject(element).getString("id").equals(room.getId())){
                        return element;
                    }
                }
            }
        }

        return null;
    }

    private String findTheaterKey(Theater theater){
        try(MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Theater");
            Document querry = new Document("_id", new ObjectId("605e1e30c3e6d4caaf7cb198"));
            Document result = collection.find(querry).iterator().next();

            JSONObject jo = new JSONObject(result.toJson());
            for (String element: jo.keySet()) {
                if (isInteger(element, 10)) {
                    if(jo.getJSONObject(element).getString("id").equals(theater.getId())){
                        return element;
                    }
                }
            }
        }

        return null;
    }

    private BasicDBObject convertOriginalToObject(String roomId){
        Room room = getRoomById(roomId);
        return convertRoomToObject(room);
    }

    private BasicDBObject convertOriginalTheaterToObject(String theaterId){
        Theater theater = getTheaterById(theaterId);
        return convertTheaterToObject(theater);
    }

    private BasicDBObject convertRoomToObject(Room room){

        BasicDBObject roomObj = new BasicDBObject();
        roomObj.put("id", room.getId());
        roomObj.put("theater_id", room.getTheaterId());
        roomObj.put("column_num", String.valueOf(room.getColumnNum()));
        roomObj.put("row_num", String.valueOf(room.getRowNum()));

        BasicDBObject rowObj = new BasicDBObject();
        for (int i = 0; i < room.getRowNum(); i++) {
            String row = "";
            for (int j = 0; j < room.getColumnNum(); j++) {
                switch(room.getRows()[i][j].getStatus()) {
                    case Empty:
                        row += 0;
                        break;
                    case Taken:
                        row += 1;
                        break;
                    case Removed:
                        row += 2;
                        break;
                    default:
                }
                if (j + 1 != room.getColumnNum()) {
                    row += ";";
                }
            }
            rowObj.put(String.valueOf(i), row);
        }
        roomObj.put("rows", rowObj);

        return roomObj;
    }

    public Room getRoomById(String roomId){
        Room[] rooms = getRooms();
        System.out.println();
        for (Room room : rooms){
            if (room.getId().equals(roomId)){

                return room;
            }
        }
        return null;
    }

    public Room[] getRoomsByTheaterId(String theaterId){
        Room[] rooms = getRooms();
        ArrayList<Room> correctRooms = new ArrayList<Room>();
        for (Room room : rooms){
            if (room.getTheaterId().equals(theaterId)){
                correctRooms.add(room);
            }
        }
        Room[] arr = new Room[correctRooms.size()];
        arr = correctRooms.toArray(arr);
        return arr;
    }

    private Room[] getRooms(){
        ArrayList<Room> rooms = new ArrayList<Room>();

        try(MongoClient mongoClient = new MongoClient(uri)){
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Room");
            Document querry = new Document("_id", new ObjectId("605e1dbfc3e6d4caaf7cb196"));
            Document result = collection.find(querry).iterator().next();

            JSONObject jo = new JSONObject(result.toJson());


            for (String element: jo.keySet()) {
                if (isInteger(element, 10)){
                    String roomId =  jo.getJSONObject(element).getString("id");
                    String theaterId =  jo.getJSONObject(element).getString("theater_id");
                    int rowNum =  Integer.parseInt(jo.getJSONObject(element).getString("row_num"));
                    int columnNum =  Integer.parseInt(jo.getJSONObject(element).getString("column_num"));
                    Room room = new Room(roomId, theaterId, rowNum, columnNum);

                    for (String rowElement: jo.getJSONObject(element).getJSONObject("rows").keySet()) {
                        String[] rowSplit = jo.getJSONObject(element).getJSONObject("rows").getString(rowElement).split(";");
                        for (int i = 0; i < rowSplit.length; i++){
                            room.getRows()[Integer.parseInt(rowElement)][i].setStatus(rowSplit[i].equals("0") ? SeatStatus.Empty : rowSplit[i].equals("1") ? SeatStatus.Taken : SeatStatus.Removed);
                        }
                    }
                    rooms.add(room);
                }
            }
        }

        Room[] arr = new Room[rooms.size()];
        arr = rooms.toArray(arr);
        return arr;
    }

    public Theater[] getAllTheater(){
        ArrayList<Theater> theater = new ArrayList<Theater>();

        try(MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Theater");
            Document querry = new Document("_id", new ObjectId("605e1e30c3e6d4caaf7cb198"));
            Document result = collection.find(querry).iterator().next();

            JSONObject jo = new JSONObject(result.toJson());

            for (String element: jo.keySet()) {
                if (isInteger(element, 10)) {
                    Theater temp = new Theater(jo.getJSONObject(element).getString("id"), jo.getJSONObject(element).getString("name"));
                    theater.add(temp);
                }
            }
        }

        Theater[] arr = new Theater[theater.size()];
        arr = theater.toArray(arr);
        return arr;
    }

    public Theater getTheaterById(String theaterId){
        Theater[] theaters = getAllTheater();
        for (Theater theater: theaters) {
            if (theater.getId().equals(theaterId)){
                return theater;
            }
        }
        return null;
    }

    private BasicDBObject convertTheaterToObject(Theater theater){

        BasicDBObject theaterObj = new BasicDBObject();
        theaterObj.put("id", theater.getId());
        theaterObj.put("name", theater.getName());

        return theaterObj;
    }

    public void saveNewTheater(Theater theater){
        BasicDBObject query = new BasicDBObject();
        query.put(getNewTheaterIndex(), convertTheaterToObject(theater));

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", query);

        try(MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Theater");
            collection.updateOne(new Document(), updateObject);
        }
        catch (Exception e){
            throw e;
        }
    }

    public void updateTheater(Theater theater){
        BasicDBObject query = new BasicDBObject();
        query.put(findTheaterKey(theater), convertOriginalTheaterToObject(theater.getId()));

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put(findTheaterKey(theater), convertTheaterToObject(theater));

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);

        try(MongoClient mongoClient = new MongoClient(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Theater");
            MongoCollection<Document> collection = database.getCollection("Theater");
            collection.updateOne(query, updateObject);
        }
        catch (Exception e){
            throw e;
        }
    }
}