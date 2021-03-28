package model.utils.temp;

import lombok.Getter;
import model.Order;
import model.Room;
import model.Theater;
import model.utils.interfaces.IStorage;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputData implements IStorage {    // TODO: Should be replaced by the data source of the GUI-Controller

    @Getter private ArrayList<Theater> theaters;
    @Getter private ArrayList<Room> rooms;
    @Getter private ArrayList<Order> orders;

    private static final InputData instance = new InputData();

    private InputData() {
        theaters = new ArrayList<>();
        rooms = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public static InputData getInstance() {
        return instance;
    }

    public void addTheater(Theater theater) { theaters.add(theater); }

    public void addTheaters(ArrayList<Theater> theaters) {
        this.theaters = (ArrayList<Theater>) Stream.concat(this.theaters.stream(), theaters.stream())
                .collect(Collectors.toList());
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addRooms(ArrayList<Room> rooms) {
        this.rooms = (ArrayList<Room>) Stream.concat(this.rooms.stream(), rooms.stream())
                                                        .collect(Collectors.toList());
    }

    public void addOrder(Order order) { orders.add(order); }

    public void addOrders(ArrayList<Order> orders) {
        this.orders = (ArrayList<Order>) Stream.concat(this.orders.stream(), orders.stream())
                .collect(Collectors.toList());
    }
}
