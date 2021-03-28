package model.utils.interfaces;

import model.Order;
import model.Room;
import model.Theater;

import java.util.ArrayList;

public interface IStorage {

    ArrayList<Theater> getTheaters();
    void addTheater(Theater theater);
    void addTheaters(ArrayList<Theater> theaters);

    ArrayList<Room> getRooms();
    void addRoom(Room room);
    void addRooms(ArrayList<Room> rooms);

    ArrayList<Order> getOrders();
    void addOrder(Order order);
    void addOrders(ArrayList<Order> orders);

}
