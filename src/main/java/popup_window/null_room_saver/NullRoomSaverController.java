package popup_window.null_room_saver;

import database.DatabaseHandler;
import globalControls.AlertMaker;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.WindowEvent;
import model.Room;
import model.Theater;
import model.utils.enums.SeatStatus;

import java.util.ArrayList;

public class NullRoomSaverController {
    private final NullRoomSaverView view;
    private final SeatStatus[][] seatStatuses;

    public NullRoomSaverController(NullRoomSaverView view, SeatStatus[][] seatStatuses) {
        this.view = view;
        this.seatStatuses = seatStatuses;

        this.view.init(this.seatStatuses.length, this.seatStatuses[0].length);
        initTheaterComboBox();
        this.view.getAddButton().setOnAction(this::addButtonPressed);
        this.view.getBackButton().setOnAction(this::backButtonPressed);
    }

    private void initTheaterComboBox() {
        view.getTheaterComboBox().setValue("Válasszon színházat...");
        ArrayList<String> theaterNames = new ArrayList<>();

        DatabaseHandler dbHandler = new DatabaseHandler();
        Theater[] theaters = dbHandler.getAllTheater();
        for (Theater theater : theaters) {
            theaterNames.add(theater.getName());
        }

        view.getTheaterComboBox().setItems(FXCollections.observableList(theaterNames));
    }

    private void backButtonPressed(ActionEvent actionEvent) {
        view.getBackButton().getScene().getWindow().fireEvent(new WindowEvent(view.getAddButton().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void addButtonPressed(ActionEvent actionEvent) {
        if (view.getTheaterComboBox().getValue().equals("Válasszon színházat...")) {
            AlertMaker.make(Alert.AlertType.ERROR, view.getAddButton().getScene().getWindow(), "Színház hiba", "Válassza ki azt a színházat, amihez hozzá szeretné adni a termet!");
        } else if (view.getRoomNameTextField().getText().equals("")) {
            AlertMaker.make(Alert.AlertType.ERROR, view.getAddButton().getScene().getWindow(), "Terem hiba", "Nem töltötte ki a Terem nevét!");
        } else {
            String theatherName = view.getTheaterComboBox().getValue();
            String roomName = view.getRoomNameTextField().getText();
            int rowNumber = Integer.parseInt(view.getRowNumberLabel().getText());
            int colNumber = Integer.parseInt(view.getColumnNumberLabel().getText());

            String theatherId = "";
            DatabaseHandler dbHandler = new DatabaseHandler();
            Theater[] theaters = dbHandler.getAllTheater();
            for (Theater theater : theaters) {
                if (theater.getName().equals(theatherName)) {
                    theatherId = theater.getId();
                }
            }

            Room room = new Room(theatherId, roomName, rowNumber, colNumber);
            dbHandler.saveNewRoom(room);

            for (int row = 0; row < room.getRows().length; row++) {
                for (int col = 0; col < room.getRows()[0].length; col++) {
                    if (seatStatuses[row][col] != SeatStatus.Empty) {
                        room.getSeat(row, col).setStatus(seatStatuses[row][col]);
                    }
                }
            }

            dbHandler.updateRoom(room);

            view.getAddButton().getScene().getWindow().fireEvent(new WindowEvent(view.getAddButton().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }
}
