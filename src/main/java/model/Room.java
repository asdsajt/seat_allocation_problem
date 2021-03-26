package model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Room {

    @Getter private final String id;
    @Getter private final String theaterId;
    @Getter private final int columnNum;
    @Getter private final int rowNum;
    private Seat[][] rows;

    public Room(String theaterId, int columnNum, int rowNum) {
        this.id = "RO-" + UUID.randomUUID();
        this.theaterId = theaterId;
        this.columnNum = columnNum;
        this.rowNum = rowNum;
    }

}
