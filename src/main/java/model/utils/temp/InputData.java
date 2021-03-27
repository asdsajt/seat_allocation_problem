package model.utils.temp;

import lombok.Getter;
import model.Order;
import model.Room;
import model.Theater;

import java.util.ArrayList;

public class InputData {    // TODO: Should be replaced by the data source of the GUI-Controller

    @Getter private ArrayList<Theater> theaters;
    @Getter private ArrayList<Room> rooms;
    @Getter private ArrayList<Order> orders;

    public InputData() {
        theaters = new ArrayList<>();
        rooms = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public void addTheater(Theater theater) { theaters.add(theater); }

    public void setTheaters(ArrayList<Theater> theaters) {
        this.theaters = theaters;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public void addOrder(Order order) { orders.add(order); }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
