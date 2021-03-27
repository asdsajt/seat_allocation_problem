package model;

import lombok.Getter;
import lombok.Setter;
import model.utils.enums.DataType;
import model.utils.general.IdGenerator;

import java.util.UUID;

public class Theater {

    @Getter private final String id;

    @Getter @Setter private String name;

    public Theater(String id, String mame) {
        this.id = id;
        this.name = name;
    }

    public Theater(String name) {
        this(IdGenerator.generateId(DataType.Theater), name);
    }

}
