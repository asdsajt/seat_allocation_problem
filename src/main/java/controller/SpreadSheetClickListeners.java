package controller;

import globalControls.AlertMaker;
import globalControls.CellStyles;
import javafx.beans.InvalidationListener;
import javafx.scene.control.Alert;
import javafx.scene.control.TablePosition;
import javafx.stage.Window;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

public class SpreadSheetClickListeners {
    @Getter
    private InvalidationListener cellDisableListener, allocationSelectorListener;


    @Setter
    private int groupNumber;

    /**
     * Elkészíti a kétesemény figyelőt a spreadsheethez.
     *  - cellDisableListener: az üres cellákat kattintás után törli, és a törölt cellákat kattintás után feloldja és üressé teszi
     *  - allocationSelectorListener: megnézi, hogy az adott helyre lehet-e csoportot berakni
     *     - először megvizsgálja, hogy a közvetlen a kiválasztott cella mellett lévő cellákat, hogy el lehet-e helyezni a csoport
     * @param spreadsheetView
     * @param window
     */
    public SpreadSheetClickListeners(SpreadsheetView spreadsheetView, Window window) {
        groupNumber = 1;

        cellDisableListener = observable -> {
            try {
                TablePosition cellpos = spreadsheetView.getSelectionModel().getSelectedCells().get(0);
                SpreadsheetCell cell = spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cellpos.getColumn());
                if (cell.getStyle().equals(CellStyles.NORMAL_CELL_STYLE)) {
                    cell.setStyle(CellStyles.BLOCKED_CELL_STYLE);
                } else if (cell.getStyle().equals(CellStyles.BLOCKED_CELL_STYLE)) {
                    cell.setStyle(CellStyles.NORMAL_CELL_STYLE);
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
                if (cell.getStyle().equals(CellStyles.NORMAL_CELL_STYLE)) {
                    String error = "";
                    int index;

                    //összetartozás
                    if (cell.getColumn() != spreadsheetView.getGrid().getColumnCount() - 1 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE) &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getText().equals("1")) {
                        error = "HELY_KIHAGYAS";
                    } else if (cell.getColumn() != 0 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE) &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getText().equals("1")) {
                        error = "HELY_KIHAGYAS";
                    } else if (cell.getColumn() != spreadsheetView.getGrid().getColumnCount() - 1 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE) &&
                            !spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getText().equals(groupNumber + "")) {
                        error = "HELY_KIHAGYAS";
                    } else if (cell.getColumn() != 0 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE) &&
                            !spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getText().equals(groupNumber + "")) {
                        error = "HELY_KIHAGYAS";
                    } else if ((cell.getColumn() != spreadsheetView.getGrid().getColumnCount() - 1 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE)) ||
                            (cell.getColumn() != 0 &&
                                    spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE))) {
                        int count = 0;
                        index = cell.getColumn() + 1;
                        while (index != spreadsheetView.getGrid().getColumnCount() &&
                                spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index).getText().equals(groupNumber + "")) {
                            count++;
                            index++;
                        }
                        index = cell.getColumn() - 1;
                        while (index != -1 && spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index).getText().equals(groupNumber + "")) {
                            if (spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index).getText().equals(groupNumber + ""))
                                count++;
                            index--;
                        }
                        if (count >= groupNumber)
                            error = "HELY_KIHAGYAS"; //megtelt a csoport
                    } else {
                        //blocked cell / másik csoport vizsgálat
                        int rightBlackPos = -1, leftBlackPos = -1;
                        index = cell.getColumn();
                        while (index != spreadsheetView.getGrid().getColumnCount() && rightBlackPos == -1) {
                            if (spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index).getStyle().equals(CellStyles.BLOCKED_CELL_STYLE) ||
                                    ((index < spreadsheetView.getGrid().getColumnCount() - 1) && spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index + 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE))) {
                                rightBlackPos = index;
                            }
                            index++;
                        }
                        index = cell.getColumn();
                        while (index != -1 && leftBlackPos == -1) {
                            if (spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index).getStyle().equals(CellStyles.BLOCKED_CELL_STYLE) ||
                                    ((index > 0) && spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index - 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE))) {
                                leftBlackPos = index;
                            }
                            index--;
                        }
                        if (((rightBlackPos == -1 ? spreadsheetView.getGrid().getColumnCount() : rightBlackPos) - leftBlackPos - 1) < groupNumber) {
                            error = "NEM_FER_EL"; //nem fér el hiba
                        } else {
                            //kettő közé behelyezés
                            if (cell.getColumn() != 0 && cell.getColumn() != spreadsheetView.getGrid().getColumnCount() &&
                                    spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE) &&
                                    spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE)) {
                                error = "KET_CSOPORT_KOZOTT"; //kettő közé nem lehet behelyezni
                            }
                        }
                    }

                    if (error.equals("")) {
                        cell.setItem(groupNumber + "");
                        cell.setStyle(CellStyles.SELECTED_CELL_STYLE);
                    } else {
                        alertMaker(window, error);
                    }
                } else if (cell.getStyle().equals(CellStyles.SELECTED_CELL_STYLE)) {
                    if (cell.getColumn() != 0 && cell.getColumn() != spreadsheetView.getGrid().getColumnCount() &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE) &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE)) {
                        alertMaker(window, "KIVETEL_HIBA");
                    } else {
                        cell.setStyle(CellStyles.NORMAL_CELL_STYLE);
                        cell.setItem("");
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void alertMaker(Window window, String msg) {
        switch (msg) {
            case "KET_CSOPORT_KOZOTT":
            case "HELY_KIHAGYAS":
                AlertMaker.make(Alert.AlertType.ERROR, window, "Hely kihagyás hiba", "Két csoport között 1 üres helyet kell hagyni, az egészségügyi szabályok miatt!");
                break;
            case "NEM_FER_EL":
                AlertMaker.make(Alert.AlertType.ERROR, window, "Kis hely hiba", "Nem lehet elhelyezni a " + groupNumber + " fős csoportot a kijelölt helyre, mert nem fér el!");
                break;
            case "KIVETEL_HIBA":
                AlertMaker.make(Alert.AlertType.ERROR, window, "Csoport bontás hiba", "Nem lehet a csoport közepéből kivenni embert!");
        }
    }
}
