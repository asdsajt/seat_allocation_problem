package solver.greedy;

import controller.CellStyles;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GreedySolver {

    private SpreadsheetView roomSpreadSheetView;
    private ArrayList<GroupElement> groupElementArrayList;

    public GreedySolver(SpreadsheetView spreadsheetView, String groupDefinition) {
        this.roomSpreadSheetView = spreadsheetView;
        this.groupElementArrayList = new ArrayList<>();
        createGroupElements(groupDefinition);
        prepareSpreadSheet();
    }

    private void prepareSpreadSheet() {
        for (ObservableList<SpreadsheetCell> row : roomSpreadSheetView.getGrid().getRows()) {
            for (SpreadsheetCell cell : row) {
                if (cell.getStyle().equals(CellStyles.SELECTED_CELL_STYLE)) {
                    cell.setStyle(CellStyles.NORMAL_CELL_STYLE);
                    cell.setItem("");
                }
            }
        }
    }

    private void createGroupElements(String groupDefinition) {
        String[] splitted = groupDefinition.split(";");
        Arrays.stream(splitted).forEach(s -> {
            try {
                groupElementArrayList.add(new GroupElement(Integer.parseInt(s)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Grid solve() {
        Grid grid = roomSpreadSheetView.getGrid();
        for (GroupElement groupElement : groupElementArrayList) {
            int rowId = 0;
            System.out.println(grid.getRows().size());
            while(rowId < grid.getRows().size() && !groupElement.isPlaced()) {
                System.out.println("asdf");
                int cellId = 0;
                while(cellId < grid.getRows().get(rowId).size() && !groupElement.isPlaced()) {
                    int start = cellId;
                    while(start < grid.getRows().get(rowId).size() &&
                            (grid.getRows().get(rowId).get(start).getStyle().equals(CellStyles.SELECTED_CELL_STYLE) ||
                                    grid.getRows().get(rowId).get(start).getStyle().equals(CellStyles.BLOCKED_CELL_STYLE))) {
                        start++;
                    }
                    if(start > 0 && grid.getRows().get(rowId).get(start - 1).getStyle().equals(CellStyles.SELECTED_CELL_STYLE))
                        start += 1;

                    int end = start;
                    while(end < grid.getRows().get(rowId).size() &&
                            grid.getRows().get(rowId).get(end).getStyle().equals(CellStyles.NORMAL_CELL_STYLE)) {
                        end++;
                    }

                    System.out.println(start + " - " + end);
                    if(end < grid.getRows().get(rowId).size() && grid.getRows().get(rowId).get(end).getStyle().equals(CellStyles.SELECTED_CELL_STYLE))
                        end -= 1;
                    System.out.println(groupElement.getGroupSize() + ": "+start + " - " + end + " sor: " + rowId);
                    if(end - start >= groupElement.getGroupSize()) {
                        for(int j = 0; j < groupElement.getGroupSize(); j++) {
                            grid.getRows().get(rowId).get(start + j).setStyle(CellStyles.SELECTED_CELL_STYLE);
                            grid.getRows().get(rowId).get(start + j).setItem(groupElement.getGroupSize() + "");
                            groupElement.setPlaced(true);
                        }
                    } else {
                        cellId = end + groupElement.getGroupSize();
                    }
                }
                rowId++;
            }
        }

        String str = "";
        for (GroupElement groupElement : groupElementArrayList) {
            if(!groupElement.isPlaced())
                str += groupElement.getGroupSize() + ", ";
        }

        if(str.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.initOwner(roomSpreadSheetView.getScene().getWindow());
            alert.setTitle("Mohó megoldó siker");
            alert.setContentText("A Mohó algoritmusnak sikerült megoldani a feladatot!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.initOwner(roomSpreadSheetView.getScene().getWindow());
            alert.setTitle("Mohó megoldó hiba");
            alert.setContentText("A Mohó algoritmusnak nem sikerült megoldani a feladatot!\nEzeket a csoportokat nem sikerült elhelyezni: " + str.substring(0, str.length()-2));
            alert.show();
        }

        return grid;
    }
}
