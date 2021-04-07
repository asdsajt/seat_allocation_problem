package model;

import lombok.Getter;
import lombok.Setter;
import model.utils.enums.DataType;
import model.utils.general.IdGenerator;

public class Theater {

    @Getter private final String id;
    @Getter @Setter private String name;

    /**
     * Use this to import already existing Theaters!
     */
    public Theater(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Use this to instantiate a new Theater!
     */
    public Theater(String name) {
        this(IdGenerator.generateId(DataType.Theater), name);
    }

    @Override
    public String toString() {
        return "Theater{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
