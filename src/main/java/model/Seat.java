package model;

import lombok.Getter;
import lombok.Setter;
import model.utils.enums.SeatStatus;

public class Seat {

    @Getter private final String position;
    @Getter private final String roomId;

    @Getter @Setter private SeatStatus status;
    @Getter @Setter private int orderId;

    public Seat(String position, String roomId) {
        this.position = position;
        this.roomId = roomId;
    }

}
