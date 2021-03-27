package controller;

import javafx.beans.InvalidationListener;
import javafx.scene.control.TablePosition;
import lombok.Getter;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

public class SpreadSheetClickListeners {
    @Getter
    private InvalidationListener cellDisableListener, allocationSelectorListener;

    private final String blockedStyle = "-fx-background-color: #101010;";
    @Getter
    private final String normalStyle = "-fx-background-color: transparent;";
    private final String selectedCellStyle = "-fx-background-color: #77ff77;";

    public SpreadSheetClickListeners(SpreadsheetView spreadsheetView) {
        cellDisableListener = observable -> {
            try {
                TablePosition cellpos = spreadsheetView.getSelectionModel().getSelectedCells().get(0);
                SpreadsheetCell cell = spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cellpos.getColumn());
                if (cell.getStyle().equals(normalStyle)) {
                    cell.setStyle(blockedStyle);
                } else {
                    cell.setStyle(normalStyle);
                }

            } catch (IndexOutOfBoundsException ignore) {
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        allocationSelectorListener = observable -> {
            try {
                TablePosition cellpos = spreadsheetView.getSelectionModel().getSelectedCells().get(0);
                SpreadsheetCell cell = spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cellpos.getColumn());
                if (cell.getStyle().equals(normalStyle)) {
                    cell.setStyle(selectedCellStyle);
                } else if (cell.getStyle().equals(selectedCellStyle)){
                    cell.setStyle(normalStyle);
                }

            } catch (IndexOutOfBoundsException ignore) {
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
