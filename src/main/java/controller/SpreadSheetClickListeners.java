package controller;

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

    private final String blockedStyle = "-fx-background-color: #101010;";
    @Getter
    private final String normalStyle = "-fx-background-color: transparent;";
    private final String selectedCellStyle = "-fx-background-color: #77ff77; -fx-font-size: 18px; -fx-alignment: center; -fx-font-weight: bold";
    @Setter
    private int groupNumber;

    public SpreadSheetClickListeners(SpreadsheetView spreadsheetView, Window window) {
        groupNumber = 1;

        cellDisableListener = observable -> {
            try {
                TablePosition cellpos = spreadsheetView.getSelectionModel().getSelectedCells().get(0);
                SpreadsheetCell cell = spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cellpos.getColumn());
                if (cell.getStyle().equals(normalStyle)) {
                    cell.setStyle(blockedStyle);
                } else if (cell.getStyle().equals(blockedStyle)) {
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
                    String error = "";
                    int index;

                    //összetartozás
                    if (cell.getColumn() != spreadsheetView.getGrid().getColumnCount() - 1 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(selectedCellStyle) &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getText().equals("1")) {
                        error = "HELY_KIHAGYAS";
                    } else if (cell.getColumn() != 0 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(selectedCellStyle) &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getText().equals("1")) {
                        error = "HELY_KIHAGYAS";
                    } else if (cell.getColumn() != spreadsheetView.getGrid().getColumnCount() - 1 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(selectedCellStyle) &&
                            !spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getText().equals(groupNumber + "")) {
                        error = "HELY_KIHAGYAS";
                    } else if (cell.getColumn() != 0 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(selectedCellStyle) &&
                            !spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getText().equals(groupNumber + "")) {
                        error = "HELY_KIHAGYAS";
                    } else if ((cell.getColumn() != spreadsheetView.getGrid().getColumnCount() - 1 &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(selectedCellStyle)) ||
                            (cell.getColumn() != 0 &&
                                    spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(selectedCellStyle))) {
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
                            if (spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index).getStyle().equals(blockedStyle) ||
                                    ((index < spreadsheetView.getGrid().getColumnCount() - 1) && spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index + 1).getStyle().equals(selectedCellStyle))) {
                                rightBlackPos = index;
                            }
                            index++;
                        }
                        index = cell.getColumn();
                        while (index != -1 && leftBlackPos == -1) {
                            if (spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index).getStyle().equals(blockedStyle) ||
                                    ((index > 0) && spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(index - 1).getStyle().equals(selectedCellStyle))) {
                                leftBlackPos = index;
                            }
                            index--;
                        }
                        if (((rightBlackPos == -1 ? spreadsheetView.getGrid().getColumnCount() : rightBlackPos) - leftBlackPos - 1) < groupNumber) {
                            error = "NEM_FER_EL"; //nem fér el hiba
                        } else {
                            //kettő közé behelyezés
                            if (cell.getColumn() != 0 && cell.getColumn() != spreadsheetView.getGrid().getColumnCount() &&
                                    spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(selectedCellStyle) &&
                                    spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(selectedCellStyle)) {
                                error = "KET_CSOPORT_KOZOTT"; //kettő közé nem lehet behelyezni
                            }
                        }
                    }

                    if (error.equals("")) {
                        cell.setItem(groupNumber + "");
                        cell.setStyle(selectedCellStyle);
                    } else {
                        alertMaker(window, error);
                    }
                } else if (cell.getStyle().equals(selectedCellStyle)) {
                    if (cell.getColumn() != 0 && cell.getColumn() != spreadsheetView.getGrid().getColumnCount() &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() - 1).getStyle().equals(selectedCellStyle) &&
                            spreadsheetView.getGrid().getRows().get(cellpos.getRow()).get(cell.getColumn() + 1).getStyle().equals(selectedCellStyle)) {
                        alertMaker(window, "KIVETEL_HIBA");
                    } else {
                        cell.setStyle(normalStyle);
                        cell.setItem("");
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void alertMaker(Window window, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.initOwner(window);

        switch (msg) {
            case "KET_CSOPORT_KOZOTT":
            case "HELY_KIHAGYAS":
                alert.setContentText("Két csoport között 1 üres helyet kell hagyni, az egészségügyi szabályok miatt!");
                alert.setTitle("Hely kihagyás hiba");
                break;
            case "NEM_FER_EL":
                alert.setContentText("Nem lehet elhelyezni a " + groupNumber + " fős csoportot a kijelölt helyre, mert nem fér el!");
                alert.setTitle("Kis hely hiba");
                break;
            case "KIVETEL_HIBA":
                alert.setContentText("Nem lehet a csoport közepéből kivenni embert!");
                alert.setTitle("Csoport bontás hiba");
        }
        alert.show();
    }
}
