package model;

import lombok.Getter;
import model.utils.enums.DataType;
import model.utils.general.IdGenerator;

public class Order {

    @Getter private final String id;
    @Getter private final String roomId;

    public Order(String id, String roomId) {
        this.id = id;
        this.roomId = roomId;
    }

    public Order(String roomId) {
        this(IdGenerator.generateId(DataType.Order), roomId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
