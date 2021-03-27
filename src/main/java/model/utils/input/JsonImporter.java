package model.utils.input;

import lombok.Getter;
import model.Room;
import model.Theater;

import java.util.ArrayList;

public class JsonImporter extends Importer{

    @Getter private ArrayList<Theater> theaters;
    @Getter private ArrayList<Room> rooms;

    @Override
    public final void importFile(String filePath) {

    }

}
