package controller;

import globalControls.AlertMaker;
import globalControls.CellStyles;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import popup_window.room_adder.RoomAdderController;
import popup_window.room_adder.RoomAdderView;
import popup_window.theater_adder.TheatherAdderController;
import popup_window.theater_adder.TheatherAdderView;
import solver.greedy.GreedySolver;
import view.View;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {

    private final View view;
    private final SpreadSheetClickListeners spreadSheetListeners;


    public Controller(View view) {
        this.view = view;
        view.init();
        spreadSheetListeners = new SpreadSheetClickListeners(this.view.getRoomSpreadSheetView(), this.view.getRoomSpreadSheetView().getScene().getWindow());

        initComboBoxes();
        configureSpreadSheet(Integer.parseInt(view.getRowNumberLabel().getText()),
                Integer.parseInt(view.getColumnNumberLabel().getText()));
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
        ArrayList<String> theaterNames = new ArrayList<>();
        //todo: itt le kell kérni a db-ben lévő színházak neveit és eltárolni a theaterNames-be
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
        view.getSolveButton().setOnAction(this::solverPressed);
        view.getAddNewTheaterButton().setOnAction(this::addTheater);
        view.getAddNewRoomButton().setOnAction(this::addRoom);
    }

    private void refreshRoomData(ActionEvent actionEvent) {
        String roomName = view.getRoomComboBox().getValue();
        if(!roomName.equals("Válasszon termet...")) {
            //todo: nekem kellene a roomName alapján ide a Room
        }
    }

    private void fillRoomComboBoxData(ActionEvent actionEvent) {
        ArrayList<String> roomNames = new ArrayList<>();
        //todo: itt le kell kérni a db-ben lévő színházhoz tartozó termek neveit és eltárolni a roomNames-be
        view.getRoomComboBox().setItems(FXCollections.observableList(roomNames));
    }

    private void addTheater(ActionEvent actionEvent) {
        TheatherAdderView theatherAdderView = new TheatherAdderView();
        theatherAdderView.initOwner(view.getAddNewTheaterButton().getScene().getWindow());
        theatherAdderView.initModality(Modality.APPLICATION_MODAL);
        theatherAdderView.setOnCloseRequest(event -> {
            try {
                theatherAdderView.close();
                System.out.println("ablak bezár");
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
                System.out.println("ablak bezár");
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
     *
     * @param rowNumber:    gridben lévő sorok száma
     * @param columnNumber: gridben lévő oszlopok száma
     */
    private void configureSpreadSheet(int rowNumber, int columnNumber) {
        view.getRoomSpreadSheetView().setGrid(null);

        GridBase gridBase = createGrid(rowNumber, columnNumber);

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
                col.setMaxWidth(maxSize.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * SpreadSheetView-ban megjelenő Griden lévő cellák inicializálása
     *
     * @param rowNumber:    sor szám
     * @param columnNumber: oszlop szám
     * @return
     */
    private GridBase createGrid(int rowNumber, int columnNumber) {
        GridBase gridBase = new GridBase(rowNumber, columnNumber);
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int i = 0; i < rowNumber; i++) {
            ObservableList<SpreadsheetCell> rowList = FXCollections.observableArrayList();
            for (int j = 0; j < columnNumber; j++) {
                SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(i, j, 1, 1, "");
                cell.setStyle(CellStyles.NORMAL_CELL_STYLE);
                rowList.add(cell);
            }
            rows.add(rowList);
        }
        gridBase.setRows(rows);
        return gridBase;
    }
}
