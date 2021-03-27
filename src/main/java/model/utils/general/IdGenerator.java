package model.utils.general;

import model.utils.enums.DataType;

import java.util.UUID;

public class IdGenerator {

    public static String generateId(DataType dataType) {
        switch (dataType) {
            case Theater:
                return "TH-" + UUID.randomUUID();
            case Room:
                return "RO-" + UUID.randomUUID();
            case Order:
                return "OR-" + UUID.randomUUID();
            default:
                return "" + UUID.randomUUID();
        }
    }

}
