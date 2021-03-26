package model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Theater {

    @Getter private final String id;

    @Getter @Setter private String name;

    public Theater(String name) {
        this.id = "TH-" + UUID.randomUUID();
    }

}
