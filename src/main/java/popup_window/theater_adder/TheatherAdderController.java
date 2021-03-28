package popup_window.theater_adder;

import database.DatabaseHandler;
import globalControls.AlertMaker;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.WindowEvent;
import model.Room;
import model.Theater;
import model.utils.interfaces.IStorage;
import model.utils.temp.InputData;

public class TheatherAdderController {
    private final TheatherAdderView view;

    public TheatherAdderController(TheatherAdderView view) {
        this.view = view;
        this.view.init();

        this.view.getAddButton().setOnAction(this::addButtonPressed);
        this.view.getBackButton().setOnAction(this::backButtonPressed);
    }

    private void backButtonPressed(ActionEvent actionEvent) {
        view.getBackButton().getScene().getWindow().fireEvent(new WindowEvent(view.getBackButton().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void addButtonPressed(ActionEvent actionEvent) {
        if (view.getTheaterNameTextField().getText().equals("")) {
            AlertMaker.make(Alert.AlertType.ERROR, view.getAddButton().getScene().getWindow(), "Színház hiba", "Nem töltötte ki a színház nevét!");
        } else {
            String theaterName = view.getTheaterNameTextField().getText();

            Theater newTheater = new Theater(theaterName);
            DatabaseHandler dbHandler = new DatabaseHandler();
            dbHandler.saveNewTheater(newTheater);

            Theater storedTheater = storeNewTheater(theaterName, InputData.getInstance());
            if (view.getRoomNameTextField().getText().equals("")) {
                view.getAddButton().getScene().getWindow().fireEvent(new WindowEvent(view.getAddButton().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
            } else {
                if (view.getRowNumberTextField().getText().equals("")) {
                    AlertMaker.make(Alert.AlertType.ERROR, view.getAddButton().getScene().getWindow(), "Sor szám hiba", "Nem töltötte ki a sorok száma mezőt!");
                } else if (view.getColumnNumberTextField().getText().equals("")) {
                    AlertMaker.make(Alert.AlertType.ERROR, view.getAddButton().getScene().getWindow(), "Oszlop szám hiba", "Nem töltötte ki a oszlopok száma mezőt!");
                } else {
                    String roomName = view.getRoomNameTextField().getText();
                    int rowNumber = Integer.parseInt(view.getRowNumberTextField().getText());
                    int colNumber = Integer.parseInt(view.getColumnNumberTextField().getText());

                    Room newRoom = new Room(newTheater.getId(), roomName, rowNumber, colNumber);
                    dbHandler.saveNewRoom(newRoom);

                    storeNewRoom(storedTheater, roomName, rowNumber, colNumber, InputData.getInstance());
                    view.getAddButton().getScene().getWindow().fireEvent(new WindowEvent(view.getAddButton().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
                }
            }

        }


    }

    private Theater storeNewTheater(String theaterName, IStorage storage) {
        Theater newTheater = new Theater(theaterName);
        storage.addTheater(newTheater);
        return newTheater;
    }

    private Room storeNewRoom(Theater theater, String name, int rowNum, int colNum,  IStorage storage) {
        Room newRoom = new Room(theater.getId(), name, rowNum, colNum);
        storage.addRoom(newRoom);
        return newRoom;
    }

}
