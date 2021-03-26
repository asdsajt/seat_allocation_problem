package model;

import lombok.Data;

public @Data class Room {

    private int id;
    private int theaterId;
    private int columnNum;
    private int rowNum;
    private Seat[][] rows;

//    public Room(int id, int theaterId, int columnNum, int rowNum) {
//        this.id = id;
//        this.theaterId = theaterId;
//        this.columnNum = columnNum;
//        this.rowNum = rowNum;
//
//    }

}
