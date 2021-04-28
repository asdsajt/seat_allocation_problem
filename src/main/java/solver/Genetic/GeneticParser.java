package solver.Genetic;

import globalControls.CellStyles;
import javafx.scene.control.Alert;
import model.Room;
import model.utils.enums.SeatStatus;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.util.*;

public class GeneticParser {

    int[] room;
    int rowNumber;
    int colNumber;
    Map orders;
    int orderSum = 0;
    Room oldRoom;
    private SpreadsheetView roomSpreadSheetView;

    public GeneticParser(Room room, int rowNumber, int colNumber, String orders, SpreadsheetView spreadsheetView){
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
        this.orders = stringToMap(orders);
        this.room = roomToArray(room);
        this.oldRoom = room;
        this.roomSpreadSheetView = spreadsheetView;
    }

    Map stringToMap(String text){

        Map back = new HashMap();
        String[] temp = text.split(";");
        int[] temp2 = new int[temp.length];
        for (int i = 0; i < temp.length; i++){
            temp2[i] = Integer.parseInt(temp[i]);
        }

        for (int i = 0; i < temp2.length; i++){
            increment(back, temp2[i]);
        }
        return back;
    }

    public static<K> void increment(Map<K, Integer> map, K key)
    {
        map.putIfAbsent(key, 0);
        map.put(key, map.get(key) + 1);
    }

    int[] roomToArray(Room room){
        int[] newRoom = new int[rowNumber*colNumber];
        for(int i = 0; i < rowNumber; i++){
            for(int j = 0; j < colNumber; j++){
                if(room.getSeat(i, j).getStatus() == SeatStatus.Removed){ newRoom[i * colNumber + j] = 1; }
                else { newRoom[i * colNumber + j] = 0; }
            }
            this.room = newRoom;
        }
        return newRoom;
    }

    Room arrayToRoom(int[] solvedRoom){
        Room newRoom = oldRoom;
        for(int i = 0; i < rowNumber; i++){
            for(int j = 0; j < colNumber; j++){
                if(newRoom.getSeat(i, j).getStatus() == SeatStatus.Removed){ /* Nothing to do */ }
                else if(solvedRoom[i * colNumber + j] == 1){ newRoom.getSeat(i, j).setStatus(SeatStatus.Taken); }
                else { newRoom.getSeat(i, j).setStatus(SeatStatus.Empty); }
            }
        }
        return newRoom;
    }

    public Room executeSolver(){
        GeneticSolver solver = new GeneticSolver();

        for (Object value : orders.values()){
            orderSum += (int)value;
        }

        int[] defResult = solver.run(room, rowNumber, colNumber, orders, orderSum);

        if(defResult == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.initOwner(roomSpreadSheetView.getScene().getWindow());
            alert.setTitle("Genetikus megoldó hiba");
            alert.setContentText("A Genetikus algoritmusnak nem sikerült megoldani a feladatot!");
            alert.show();
            return null;
        }

        int[] result = deleteExtraElements(defResult);
        Room resultRoom = arrayToRoom(result);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.initOwner(roomSpreadSheetView.getScene().getWindow());
        alert.setTitle("Genetikus megoldó siker");
        alert.setContentText("A Genetikus algoritmusnak sikerült megoldani a feladatot!");
        alert.show();

        return resultRoom;
    }

    int orderSum(){
        int orderSum = 0;
        for (Object value : orders.values()){
            orderSum += (int)value;
        }
        return orderSum;
    }

    ArrayList<Integer> placeOrder(Room room){
        ArrayList<Integer> place = new ArrayList<Integer>();

        for(int i = 0; i < rowNumber; i++){
            int count = 0;
            for(int j = 0; j < colNumber; j++){
                if(room.getSeat(i, j).getStatus() == SeatStatus.Taken){
                    count++;
                }
                else{
                    if(count > 0){
                        place.add(count);
                        count = 0;
                    }
                }
            }
            if(count > 0){ place.add(count); }
        }

        return place;
    }

    int[] deleteExtraElements(int[] genes){
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        ArrayList<Integer> startI = new ArrayList<Integer>();
        ArrayList<Integer> startJ = new ArrayList<Integer>();
        ArrayList<Integer> orderLength = new ArrayList<Integer>();
        int oneCount = 0;

        Map current = new HashMap();

        for (int i = 0; i < rowNumber; i++){
            matrix.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < rowNumber; i++){
            for (int j = 0; j < colNumber; j++){
                matrix.get(i).add(genes[i * colNumber + j]);
            }
        }
        for (int i = 0; i < rowNumber; i++){
            boolean first = true;
            for (int j = 0; j < colNumber; j++){
                if (matrix.get(i).get(j) == 1){
                    if (first){
                        startI.add(i);
                        startJ.add(j);
                        first = false;
                    }
                    oneCount++;
                }
                else if(matrix.get(i).get(j) == 0 && oneCount > 0){
                    orderLength.add(oneCount);
                    oneCount = 0;
                    first = true;
                }
            }
            if(oneCount > 0){
                orderLength.add(oneCount);
                oneCount = 0;
                first = true;
            }
        }
        for (int i = 0; i < orderLength.size(); i++){
            increment(current, orderLength.get(i));
        }

        Set ordersKeys = orders.keySet();
        Iterator it = current.keySet().iterator();

        while(it.hasNext())
        {
            int key=(int)it.next();
            if(ordersKeys.contains(key)){
                if ((int)current.get(key) > (int)orders.get(key)){
                    for (int j = 0; j < (int)current.get(key) - (int)orders.get(key); j++) {
                        int firstIndex = findFirstIndex(orderLength, key);
                        for (int i = 0; i < key; i++) {
                            matrix.get(startI.get(firstIndex)).set(startJ.get(firstIndex) + i, 0);
                        }
                        startI.remove(firstIndex);
                        startJ.remove(firstIndex);
                        orderLength.remove(firstIndex);
                    }
                }
            }
            else{
                for (int j = 0; j < (int)current.get(key); j++) {
                    int firstIndex = findFirstIndex(orderLength, key);
                    for (int i = 0; i < key; i++) {
                        matrix.get(startI.get(firstIndex)).set(startJ.get(firstIndex) + i, 0);
                    }
                    startI.remove(firstIndex);
                    startJ.remove(firstIndex);
                    orderLength.remove(firstIndex);
                }
            }
        }

        int[] back = new int[rowNumber * colNumber];
        for (int i = 0; i < rowNumber; i++){
            for (int j = 0; j < colNumber; j++){
                back[i * colNumber + j] = matrix.get(i).get(j);
            }
        }
        return back;
    }

    int findFirstIndex(ArrayList<Integer> arr, int length){
        for (int i = 0; i < arr.size(); i++){
            if (length == arr.get(i)) return i;
        }
        return -1;
    }

    public Grid roomToView(Room room){
        Grid grid = roomSpreadSheetView.getGrid();
        ArrayList<Integer> places = placeOrder(room);

        for(int i = 0; i < rowNumber; i++){
            for(int j = 0; j < colNumber; j++){
                if(room.getSeat(i, j).getStatus() != SeatStatus.Removed){
                    grid.getRows().get(i).get(j).setStyle(CellStyles.NORMAL_CELL_STYLE);
                    grid.getRows().get(i).get(j).setItem("");
                }
            }
        }

        for(int i = 0; i < rowNumber; i++){
            int count = 0;
            for(int j = 0; j < colNumber; j++){
                if(room.getSeat(i, j).getStatus() == SeatStatus.Taken){
                    grid.getRows().get(i).get(j).setStyle(CellStyles.SELECTED_CELL_STYLE);
                    grid.getRows().get(i).get(j).setItem(places.get(0) + "");
                    count++;
                }
                else{
                    if(count > 0){
                        places.remove(0);
                        count = 0;
                    }
                }
            }
            if(count > 0){ places.remove(0); }
        }
        return grid;
    }

}
