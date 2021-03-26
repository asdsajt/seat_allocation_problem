package model;

import lombok.Data;
import model.utils.SeatStatus;

public @Data class Seat {

    private SeatStatus status;
    private int orderId;

}
