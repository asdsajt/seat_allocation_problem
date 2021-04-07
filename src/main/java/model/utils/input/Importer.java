package model.utils.input;

public abstract class Importer {

    public static final int nullIntValue = -999;

    public abstract void importFile(String filePath);

    protected static String parseString(Object object) {
        try {
            return object.toString();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    protected static int parseInt(Object object) {
        String value = parseString(object);
        if (value == null) { return nullIntValue; }
        return Integer.parseInt(value);
    }

}
