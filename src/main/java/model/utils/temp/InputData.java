package model.utils.temp;

import lombok.Getter;
import model.Room;
import model.Theater;

import java.util.ArrayList;

public class InputData {

    @Getter private ArrayList<Theater> theaters;
    @Getter private ArrayList<Room> rooms;

    public void addTheater(Theater theater) {
        theaters.add(theater);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

}
