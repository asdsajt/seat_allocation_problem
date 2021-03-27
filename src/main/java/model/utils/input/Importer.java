package model.utils.input;

import model.utils.temp.InputData;

public abstract class Importer {

    public abstract void importFile(String filePath, InputData storage);

}
