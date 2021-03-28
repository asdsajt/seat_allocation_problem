package model;

import lombok.Getter;
import model.utils.enums.DataType;
import model.utils.enums.SeatStatus;
import model.utils.general.IdGenerator;

import java.util.ArrayList;

public class Room {

    @Getter private final String id;
    @Getter private final String theaterId;
    @Getter private final String name;
    @Getter private final int columnNum;
    @Getter private final int rowNum;
    @Getter private Seat[][] rows;

    // Only for importing with valid IDs
    public Room(String id, String theaterId, String name, int columnNum, int rowNum) {
        this.id = id;
        this.theaterId = theaterId;
        this.name = name;
        this.columnNum = columnNum;
        this.rowNum = rowNum;
        generateSeats();
    }

    // Use this to instantiate a new Room
    public Room(String theaterId, String name, int columnNum, int rowNum) {
        this(IdGenerator.generateId(DataType.Room), theaterId, name, columnNum, rowNum);
    }

    public Room(String name, int columnNum, int rowNum) {
        this("TH-Unknown", name, columnNum, rowNum);
    }

    public Seat[] getRow(int rowNum) {
        return this.rows[rowNum];
    }

    public Seat getSeat(int rowNum, int colNum) {
        return this.rows[rowNum][colNum];
    }

    public void setSeat(int rowNum, int colNum, Seat seat) {
        this.rows[rowNum][colNum] = seat;
    }

    private void generateSeats() {
        rows = new Seat[this.rowNum][this.columnNum];
        for(int i = 0; i < this.rowNum; i++) {
            for(int j = 0; j < this.columnNum; j++) {
                String position = Seat.generatePositionString(i, j);
                Seat currentSeat = new Seat(position, this.id);
                currentSeat.setStatus(SeatStatus.Empty);
                rows[i][j] = currentSeat;
            }
        }
    }

    @Override
    public String toString() {
        ArrayList<String> seatInfo = new ArrayList<>();
        for (int r = 0; r < this.rowNum; r++) {
            for (int c = 0; c < this.columnNum; c++) {
                seatInfo.add(getSeat(r, c).toString());
            }
        }
        return "Room{" +
                "id='" + id + '\'' +
                ", theaterId='" + theaterId + '\'' +
                ", name=" + name +
                ", columnNum=" + columnNum +
                ", rowNum=" + rowNum +
                ", rows=" + String.join(", ", seatInfo) +
                '}';
    }
}
