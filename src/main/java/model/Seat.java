package model;

import lombok.Getter;
import lombok.Setter;
import model.utils.SeatStatus;

public class Seat {

    @Getter private String roomId;

    @Getter @Setter private SeatStatus status;
    @Getter @Setter private int orderId;

    public Seat(String roomId) {
        this.roomId = roomId;
    }

}
