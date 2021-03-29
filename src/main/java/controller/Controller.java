package controller;

import com.mongodb.lang.Nullable;
import database.DatabaseHandler;
import globalControls.AlertMaker;
import globalControls.CellStyles;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import model.Room;
import model.Seat;
import model.Theater;
import model.utils.enums.SeatStatus;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import popup_window.null_room_saver.NullRoomSaverController;
import popup_window.null_room_saver.NullRoomSaverView;
import popup_window.room_adder.RoomAdderController;
import popup_window.room_adder.RoomAdderView;
import popup_window.theater_adder.TheatherAdderController;
import popup_window.theater_adder.TheatherAdderView;
import solver.greedy.GreedySolver;
import view.View;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {

    private final View view;
    private final SpreadSheetClickListeners spreadSheetListeners;
    private Room currentRoom;


    public Controller(View view) {
        this.view = view;
        this.currentRoom = null;
        view.init();
        spreadSheetListeners = new SpreadSheetClickListeners(this.view.getRoomSpreadSheetView(), this.view.getRoomSpreadSheetView().getScene().getWindow());

        initComboBoxes();
        configureSpreadSheet(Integer.parseInt(view.getRowNumberLabel().getText()),
                Integer.parseInt(view.getColumnNumberLabel().getText()), null);
        createListeners();
    }

    /**
     * ComboBox értékekek inicializálása
     */
    private void initComboBoxes() {
        view.getGroupNumberComboBox().setItems(ComboBoxStrings.GROUP_SELECTOR_STRINGS);
        view.getGroupNumberComboBox().setValue(ComboBoxStrings.GROUP_SELECTOR_STRINGS.get(0));
        view.getSolveMethodComboBox().setItems(ComboBoxStrings.SOLVER_METHOD_STRINGS);
        view.getSolveMethodComboBox().setValue("Válasszon...");
        view.getTheaterComboBox().setValue("Válasszon színházat...");
        view.getRoomComboBox().setValue("Válasszon termet...");
        setTheaterComboBoxItems();

    }

    private void setTheaterComboBoxItems() {
        ArrayList<String> theaterNames = new ArrayList<>();

        DatabaseHandler dbHandler = new DatabaseHandler();
        Theater[] theaters = dbHandler.getAllTheater();
        for (Theater theater : theaters) {
            theaterNames.add(theater.getName());
        }

        view.getTheaterComboBox().setItems(FXCollections.observableList(theaterNames));
    }

    /**
     * Listenerek elkészítése a különböző beviteli mezőknek.
     */
    private void createListeners() {
        view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().addListener(spreadSheetListeners.getAllocationSelectorListener());

        view.getDisableSeatsCheckBox().selectedProperty().addListener(this::disableSeatsChanged);

        view.getGroupNumberComboBox().setOnAction(this::groupNumberChanged);
        view.getTheaterComboBox().setOnAction(this::fillRoomComboBoxData);
        view.getRoomComboBox().setOnAction(this::refreshRoomData);

        view.getSaveCurrentRoomButton().setOnAction(this::saveCurrentRoom);
        view.getSolveButton().setOnAction(this::solverPressed);
        view.getAddNewTheaterButton().setOnAction(this::addTheater);
        view.getAddNewRoomButton().setOnAction(this::addRoom);
    }

    private void saveCurrentRoom(ActionEvent actionEvent) {
        if(currentRoom == null) {
            SeatStatus[][] seatStatuses = new SeatStatus[view.getRoomSpreadSheetView().getGrid().getRowCount()][view.getRoomSpreadSheetView().getGrid().getColumnCount()];
            for (ObservableList<SpreadsheetCell> row : view.getRoomSpreadSheetView().getGrid().getRows()) {
                for (SpreadsheetCell cell : row) {
                    if(cell.getStyle().equals(CellStyles.BLOCKED_CELL_STYLE))
                        seatStatuses[cell.getRow()][cell.getColumn()] = SeatStatus.Removed;
                    else
                        seatStatuses[cell.getRow()][cell.getColumn()] = SeatStatus.Empty;
                }
            }
            NullRoomSaverView nullRoomSaverView = new NullRoomSaverView();
            nullRoomSaverView.initOwner(view.getAddNewTheaterButton().getScene().getWindow());
            nullRoomSaverView.initModality(Modality.APPLICATION_MODAL);
            nullRoomSaverView.setOnCloseRequest(event -> {
                try {
                    nullRoomSaverView.close();
                    refreshRoomComboBxDataAfterDB(nullRoomSaverView.getTheaterComboBox().getValue());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            new NullRoomSaverController(nullRoomSaverView, seatStatuses);
        } else {
            //todo az aktuális terem mentése
            //todo currentRoom tartalmazza a mentéshez szükséges infókat
        }
    }

    private void refreshRoomComboBxDataAfterDB(String modifiedTheatherName) {
        if (!modifiedTheatherName.equals("Válasszon színházat...") && modifiedTheatherName.equals(view.getTheaterComboBox().getValue())) {
            String theaterId = getTheaterIdByName(modifiedTheatherName);
            ArrayList<String> roomNames = new ArrayList<>();

            DatabaseHandler dbHandler = new DatabaseHandler();
            Room[] rooms = dbHandler.getRoomsByTheaterId(theaterId);
            for (Room room : rooms) {
                roomNames.add(room.getName());
            }

            view.getRoomComboBox().setItems(FXCollections.observableList(roomNames));
        }
    }

    private void refreshRoomData(ActionEvent actionEvent) {
        String roomName = view.getRoomComboBox().getValue();
        String theaterName = view.getTheaterComboBox().getValue();
        if(!roomName.equals("Válasszon termet...") || !theaterName.equals("Válasszon színházat...")) {

            String theaterId = getTheaterIdByName(theaterName);

            DatabaseHandler dbHandler = new DatabaseHandler();
            Room[] rooms = dbHandler.getRoomsByTheaterId(theaterId);
            for (Room room : rooms) {
                if (room.getName().equals(roomName)){
                    currentRoom = room;
                }
            }
            if (currentRoom != null) {
                view.getRowNumberLabel().setText(currentRoom.getRowNum()+"");
                view.getColumnNumberLabel().setText(currentRoom.getColumnNum()+"");
                configureSpreadSheet(currentRoom.getRowNum(), currentRoom.getColumnNum(), currentRoom.getRows());
            }
        }
    }

    private String getTheaterIdByName(String theaterName){
        DatabaseHandler dbHandler = new DatabaseHandler();
        Theater[] theaters = dbHandler.getAllTheater();
        for (Theater theater : theaters) {
            if (theater.getName().equals(theaterName)){
                return theater.getId();
            }
        }
        return null;
    }

    private void fillRoomComboBoxData(ActionEvent actionEvent) {
        ArrayList<String> roomNames = new ArrayList<>();

        String theaterName = view.getTheaterComboBox().getValue();
        String theaterId = "";

        if(!theaterName.equals("Válasszon színházat...")) {
            theaterId = getTheaterIdByName(theaterName);

            DatabaseHandler dbHandler = new DatabaseHandler();
            Room[] rooms = dbHandler.getRoomsByTheaterId(theaterId);
            for (Room room : rooms) {
                roomNames.add(room.getName());
            }
        }

        view.getRoomComboBox().setItems(FXCollections.observableList(roomNames));
    }

    private void addTheater(ActionEvent actionEvent) {
        TheatherAdderView theatherAdderView = new TheatherAdderView();
        theatherAdderView.initOwner(view.getAddNewTheaterButton().getScene().getWindow());
        theatherAdderView.initModality(Modality.APPLICATION_MODAL);
        theatherAdderView.setOnCloseRequest(event -> {
            try {
                theatherAdderView.close();
                setTheaterComboBoxItems();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        new TheatherAdderController(theatherAdderView);
    }


    private void addRoom(ActionEvent actionEvent) {
        RoomAdderView roomAdderView = new RoomAdderView();
        roomAdderView.initOwner(view.getAddNewRoomButton().getScene().getWindow());
        roomAdderView.initModality(Modality.APPLICATION_MODAL);
        roomAdderView.setOnCloseRequest(event -> {
            try {
                roomAdderView.close();
                refreshRoomComboBxDataAfterDB(roomAdderView.getTheaterComboBox().getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        new RoomAdderController(roomAdderView);
    }

    private void solverPressed(ActionEvent actionEvent) {
        if (view.getDisableSeatsCheckBox().isSelected()) {
            view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().removeListener(spreadSheetListeners.getCellDisableListener());
        } else {
            view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().removeListener(spreadSheetListeners.getAllocationSelectorListener());
        }
        switch (view.getSolveMethodComboBox().getValue()) {
            case "Válasszon...":
                AlertMaker.make(Alert.AlertType.ERROR, view.getSolveButton().getScene().getWindow(), "Megoldó hiba", "Először válasszon a megoldók közül!");
                break;
            case "Mohó algoritmus":
                GreedySolver greedySolver = new GreedySolver(view.getRoomSpreadSheetView(), view.getGroupDefinitionTextArea().getText().trim());
                view.getRoomSpreadSheetView().setGrid(greedySolver.solve());
                break;
        }
        if (view.getDisableSeatsCheckBox().isSelected()) {
            view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().addListener(spreadSheetListeners.getCellDisableListener());
        } else {
            view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().addListener(spreadSheetListeners.getAllocationSelectorListener());
        }
    }


    /**
     * Székek tiltására szolgáló checkbox change listenerje
     *
     * @param observable
     */
    private void disableSeatsChanged(Observable observable) {
        if (view.getDisableSeatsCheckBox().isSelected()) {
            view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().removeListener(spreadSheetListeners.getAllocationSelectorListener());
            view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().addListener(spreadSheetListeners.getCellDisableListener());
        } else {
            view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().removeListener(spreadSheetListeners.getCellDisableListener());
            view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().addListener(spreadSheetListeners.getAllocationSelectorListener());
        }
    }

    /**
     * Csoport változtatás action.
     *
     * @param actionEvent
     */
    private void groupNumberChanged(ActionEvent actionEvent) {
        spreadSheetListeners.setGroupNumber(Integer.parseInt(view.getGroupNumberComboBox().getValue().split(" ")[0]));
    }

    /**
     * SpreadSheetView konfigurálása
     * - elkészíti a grid-et
     * - gridben a cellák magasságát állítja
     * - spreadSheethez hozzáadja a gridet
     * - spreadSheet oszlopainak méretezése
     *  @param rowNumber :    gridben lévő sorok száma
     * @param columnNumber : gridben lévő oszlopok száma
     * @param rows: aktuális terem
     */
    private void configureSpreadSheet(int rowNumber, int columnNumber, @Nullable Seat[][] rows) {
        view.getRoomSpreadSheetView().setGrid(null);

        GridBase gridBase = createGrid(rowNumber, columnNumber, rows);

        /**
         * Automatikus méretezés, hogy ne kelljen görgetni
         */
        AtomicReference<Double> maxSize = new AtomicReference<>();
        maxSize.set(100.0);
        while ((rowNumber >= columnNumber ? rowNumber : columnNumber) * maxSize.get() > 760 && maxSize.get() != 35) {
            maxSize.set(maxSize.get() - 1);
        }

        gridBase.setRowHeightCallback(param -> {
            try {
                return maxSize.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        view.getRoomSpreadSheetView().setGrid(gridBase);
        view.getRoomSpreadSheetView().getColumns().forEach(col -> {
            try {
                col.setMinWidth(maxSize.get());
                col.setMaxWidth(maxSize.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * SpreadSheetView-ban megjelenő Griden lévő cellák inicializálása
     *
     * @param rowNumber :    sor szám
     * @param columnNumber : oszlop szám
     * @param currentRows: ha van betöltött terem, akkor a cella tiltást is elvégzi
     * @return
     */
    private GridBase createGrid(int rowNumber, int columnNumber, @Nullable Seat[][] currentRows) {
        GridBase gridBase = new GridBase(rowNumber, columnNumber);
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int i = 0; i < rowNumber; i++) {
            ObservableList<SpreadsheetCell> rowList = FXCollections.observableArrayList();
            for (int j = 0; j < columnNumber; j++) {
                SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(i, j, 1, 1, "");
                if(currentRows != null && currentRows[i][j].getStatus() == SeatStatus.Removed)
                    cell.setStyle(CellStyles.BLOCKED_CELL_STYLE);
                else
                    cell.setStyle(CellStyles.NORMAL_CELL_STYLE);
                rowList.add(cell);
            }
            rows.add(rowList);
        }
        gridBase.setRows(rows);
        return gridBase;
    }
}
