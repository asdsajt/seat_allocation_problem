package model;

import lombok.Getter;
import model.utils.SeatStatus;

import java.util.UUID;

public class Room {

    @Getter private final String id;
    @Getter private final String theaterId;
    @Getter private final int columnNum;
    @Getter private final int rowNum;
    @Getter private Seat[][] rows;

    public Room(String theaterId, int columnNum, int rowNum) {
        this.id = "RO-" + UUID.randomUUID();
        this.theaterId = theaterId;
        this.columnNum = columnNum;
        this.rowNum = rowNum;
        generateSeats();
    }

    public Room(int columnNum, int rowNum) {
        this("TH-Unknown", columnNum, rowNum);
    }

    private void generateSeats() {
        rows = new Seat[this.rowNum][this.columnNum];
        for(int i = 0; i < this.rowNum; i++) {
            for(int j = 0; j < this.columnNum; j++) {
                String position = i + ":" + j;
                Seat currentSeat = new Seat(position, this.id);
                currentSeat.setStatus(SeatStatus.empty);
                rows[i][j] = currentSeat;
            }
        }
    }

    public Seat[] getRow(int rowNum) {
        return this.rows[rowNum];
    }

    public Seat getSeat(int rowNum, int colNum) {
        return this.rows[rowNum][colNum];
    }

}
