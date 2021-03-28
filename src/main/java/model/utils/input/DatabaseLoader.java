package model.utils.input;

import database.DatabaseHandler;
import model.Room;
import model.Theater;
import model.utils.temp.InputData;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseLoader {


    public static void loadAll(InputData storage) {
        DatabaseHandler db = new DatabaseHandler();
        ArrayList<Theater> theaters = new ArrayList<>(Arrays.asList(db.getAllTheater()));
        storage.addTheaters(theaters);
        for (Theater theater : theaters) {
            ArrayList<Room> roomsInTheater = new ArrayList<>(Arrays.asList(db.getRoomsByTheaterId(theater.getId())));
            storage.addRooms(roomsInTheater);
        }
    }


}
