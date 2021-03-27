package model.utils.temp;

import lombok.Getter;
import model.Room;
import model.Theater;

import java.util.ArrayList;

public class InputData {    // TODO: Should be replaced by the data source of the GUI-Controller

    @Getter private ArrayList<Theater> theaters;
    @Getter private ArrayList<Room> rooms;

    public void addTheater(Theater theater) {
        theaters.add(theater);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

}
