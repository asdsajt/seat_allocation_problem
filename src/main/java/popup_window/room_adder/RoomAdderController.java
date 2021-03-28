package popup_window.room_adder;

import globalControls.AlertMaker;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

public class RoomAdderController {
    private final RoomAdderView view;

    public RoomAdderController(RoomAdderView view) {
        this.view = view;
        this.view.init();
        initTheaterComboBox();
        this.view.getAddButton().setOnAction(this::addButtonPressed);
        this.view.getBackButton().setOnAction(this::backButtonPressed);
    }

    private void initTheaterComboBox() {
        view.getTheaterComboBox().setValue("Válasszon színházat...");
        ArrayList<String> theaterNames = new ArrayList<>();

        //todo: itt le kell kérni a db-ben lévő színházak neveit és eltárolni a theaterNames-be

        view.getTheaterComboBox().setItems(FXCollections.observableList(theaterNames));
    }

    private void backButtonPressed(ActionEvent actionEvent) {
        view.getBackButton().getScene().getWindow().fireEvent(new WindowEvent(view.getBackButton().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void addButtonPressed(ActionEvent actionEvent) {
        if(view.getTheaterComboBox().getValue().equals("Válasszon színházat...")) {
            AlertMaker.make(Alert.AlertType.ERROR, view.getAddButton().getScene().getWindow(), "Színház hiba", "Válassza ki azt a színházat, amihez hozzá szeretné adni a termet!");
        } else if (view.getRoomNameTextField().getText().equals("")) {
            AlertMaker.make(Alert.AlertType.ERROR, view.getAddButton().getScene().getWindow(), "Terem hiba", "Nem töltötte ki a Terem nevét!");
        } else if (view.getRowNumberTextField().getText().equals("")) {
            AlertMaker.make(Alert.AlertType.ERROR, view.getAddButton().getScene().getWindow(), "Sor szám hiba", "Nem töltötte ki a sorok száma mezőt!");
        } else if (view.getColumnNumberTextField().getText().equals("")) {
            AlertMaker.make(Alert.AlertType.ERROR, view.getAddButton().getScene().getWindow(), "Oszlop szám hiba", "Nem töltötte ki a oszlopok száma mezőt!");
        } else {
            String theatherName = view.getTheaterComboBox().getValue();
            String roomName = view.getRoomNameTextField().getText();
            int rowNumber = Integer.parseInt(view.getRowNumberTextField().getText());
            int colNumber = Integer.parseInt(view.getColumnNumberTextField().getText());

            //todo: itt a színház név alapján az id-t le kell kérni még
            //todo: új terem hozzáadása a kiválasztott színházhoz

            view.getAddButton().getScene().getWindow().fireEvent(new WindowEvent(view.getAddButton().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }
}
