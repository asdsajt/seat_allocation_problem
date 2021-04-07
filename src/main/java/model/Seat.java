package model;

import lombok.Getter;
import lombok.Setter;
import model.utils.enums.SeatStatus;

public class Seat {

    @Getter private final String position;
    @Getter private final String roomId;

    @Getter @Setter private SeatStatus status;
    @Getter @Setter private String orderId;

    public Seat(String position, String roomId) {
        this.position = position;
        this.roomId = roomId;
    }

    /**
     * Use this to generate a Seat.position String!
     * @param row of the Room
     * @param column of the Room
     * @return 'row:column'
     */
    public static String generatePositionString(int row, int column) {
        return row + ":" + column;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "position='" + position + '\'' +
                ", roomId='" + roomId + '\'' +
                ", status=" + status +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
