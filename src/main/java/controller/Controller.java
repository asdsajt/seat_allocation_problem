package controller;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextField;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import view.View;

public class Controller {

    private View view;
    private InvalidationListener cellDisableListener, allocationSelectorListener;

    private String blockedStyle, normalStyle, selectedCellStyle;

    public Controller(View view) {
        this.view = view;

        blockedStyle = "-fx-background-color: #d95a5a;";
        normalStyle = "-fx-background-color: transparent;";
        selectedCellStyle = "-fx-backgroud-color: 9cfc94;";

        view.init();
        configureSpreadSheet(Integer.parseInt(view.getRowNumberTextField().getText()), Integer.parseInt(view.getColumnNumberTextField().getText()));
        createListeners();
    }

    private void createListeners() {
        cellDisableListener = observable -> {
            try {
                TablePosition cellpos = view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().get(0);
                SpreadsheetCell cell = view.getRoomSpreadSheetView().getGrid().getRows().get(cellpos.getRow()).get(cellpos.getColumn());
                if (cell.getStyle().equals(normalStyle)) {
                    cell.setStyle(blockedStyle);
                } else {
                    cell.setStyle(normalStyle);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        allocationSelectorListener = observable -> {
            try {
                TablePosition cellpos = view.getRoomSpreadSheetView().getSelectionModel().getSelectedCells().get(0);
                SpreadsheetCell cell = view.getRoomSpreadSheetView().getGrid().getRows().get(cellpos.getRow()).get(cellpos.getColumn());
                if (cell.getStyle().equals(normalStyle)) {
                    cell.setStyle(selectedCellStyle);
                } else if (cell.getStyle().equals(selectedCellStyle)){
                    cell.setStyle(normalStyle);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        view.getRowNumberTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.matches("\\d*")) {
                    view.getRowNumberTextField().setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(!newValue.equals(""))
                    configureSpreadSheet(Integer.parseInt(view.getRowNumberTextField().getText()), Integer.parseInt(view.getColumnNumberTextField().getText()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        view.getColumnNumberTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.matches("\\d*")) {
                    view.getColumnNumberTextField().setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(!newValue.equals(""))
                    configureSpreadSheet(Integer.parseInt(view.getRowNumberTextField().getText()), Integer.parseInt(view.getColumnNumberTextField().getText()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void configureSpreadSheet(int rowNumber, int columnNumber) {
        view.getRoomSpreadSheetView().setGrid(null);
        view.getRoomSpreadSheetView().setGrid(createGridBase(rowNumber, columnNumber));
        view.getRoomSpreadSheetView().getColumns().forEach(col -> {
            try {
                col.setPrefWidth(50);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Grid createGridBase(int rowNumber, int columnNumber) {
        GridBase gridBase = new GridBase(rowNumber, columnNumber);
        gridBase.setRowHeightCallback(param -> {
            try {
                return 50.0;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for(int i = 0; i < rowNumber; i++) {
            ObservableList<SpreadsheetCell> rowList = FXCollections.observableArrayList();
            for(int j = 0; j < columnNumber; j++) {
                SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(i, j, 1, 1, "");
                cell.setStyle(normalStyle);
                rowList.add(cell);
            }
            rows.add(rowList);
        }

        gridBase.setRows(rows);
        return gridBase;
    }
}
