package solver.LP;

import javafx.util.Pair;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.control.spreadsheet.Grid;
import model.utils.enums.SeatStatus;
import javafx.scene.control.Alert;
import globalControls.CellStyles;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.Room;

public class LPParser {

    int[] places;
    int[] people;
    Room oldRoom;
    SpreadsheetView roomSpreadSheetView;

    public LPParser(Room room, String orders, SpreadsheetView spreadsheetView){
        oldRoom = room;
        people = stringToArray(orders);
        places = getPlaces(room);
        roomSpreadSheetView = spreadsheetView;
    }

    public Room executeSolver(){
        LPSolver solver = new LPSolver();
        Map<Integer, Integer> res = solver.solve(places, people);

        if(res == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.initOwner(roomSpreadSheetView.getScene().getWindow());
            alert.setTitle("LP megoldó hiba");
            alert.setContentText("Az LP megoldónak nem sikerült megoldani a feladatot!");
            alert.show();
            return null;
        }

        sortPeople(res);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.initOwner(roomSpreadSheetView.getScene().getWindow());
        alert.setTitle("LP megoldó siker");
        alert.setContentText("Az LP megoldónak sikerült megoldani a feladatot!");
        alert.show();

        return arrayToRoom(mapToArray2(res));
    }

    private void sortPeople(Map<Integer, Integer> res){
        ArrayList<Integer> sorted = new ArrayList<Integer>();
        ArrayList<Integer> set = new ArrayList<Integer>();

        for (Map.Entry<Integer, Integer> entry : res.entrySet()) {
            if (!set.contains(entry.getValue())) { set.add(entry.getValue()); }
        }

        Collections.sort(set);

        for (Integer min: set) {
            for (Map.Entry<Integer, Integer> entry : res.entrySet()) {
                if (entry.getValue() == min) { sorted.add(people[entry.getKey()]); }
            }
        }

        people = convertIntegers(sorted);
    }

    private int[] stringToArray(String text){
        String[] temp = text.split(";");
        int[] temp2 = new int[temp.length];
        for (int i = 0; i < temp.length; i++){
            temp2[i] = Integer.parseInt(temp[i]);
        }
        return temp2;
    }

    private int[] getPlaces(Room room){
        int[] arr = roomToArray(room);

        ArrayList<Integer> list = new ArrayList<Integer>();

        int counter = -1;
        int[][] twoDim = new int[oldRoom.getRowNum()][oldRoom.getColumnNum()];

        for(int i = 0; i < arr.length; i++){
            if (i % oldRoom.getColumnNum() == 0) counter++;
            twoDim[counter][i % oldRoom.getColumnNum()] = arr[i];
        }

        boolean first = true;
        int count = 0;

        for(int i = 0; i < oldRoom.getRowNum(); i++) {
            for (int j = 0; j < oldRoom.getColumnNum(); j++) {
                if (twoDim[i][j] == 0) { first = false; count++; }
                else {
                    first = true;
                    list.add(count);
                    count = 0;
                }
            }
            if(!first){ list.add(count); }
            first = true;
            count = 0;
        }

        return convertIntegers(list);
    }

    private int[] mapToArray(Map<Integer, Integer> map){
        int[] arr = roomToArray(oldRoom);
        int counter = -1;
        int[][] twoDim = new int[oldRoom.getRowNum()][oldRoom.getColumnNum()];

        for(int i = 0; i < arr.length; i++){
            if (i % oldRoom.getColumnNum() == 0) counter++;
            twoDim[counter][i % oldRoom.getColumnNum()] = arr[i];
        }

        counter = 0;
        int prev = 2;
        int currentLength = 0;

        for(int i = 0; i < oldRoom.getRowNum(); i++){
            for(int j = 0; j < oldRoom.getColumnNum(); j++){
                if ( twoDim[i][j] == 0 && people[counter] > currentLength){
                    currentLength++;
                    prev = 0;
                    twoDim[i][j] = 1;
                }
                else if (people[counter] == currentLength) {
                    counter++;
                    currentLength = 0;
                    prev = 2;
                    twoDim[i][j] = 0;
                }
            }
            if(prev == 0) {
                counter++;
                currentLength = 0;
                prev = 2;
            }
        }

        for(int i = 0; i < oldRoom.getRowNum(); i++) {
            for (int j = 0; j < oldRoom.getColumnNum(); j++) {
                arr[i * oldRoom.getColumnNum() + j] = twoDim[i][j];
            }
        }

        return arr;
    }

    int[] roomToArray(Room room){
        int[] newRoom = new int[oldRoom.getRowNum()*oldRoom.getColumnNum()];
        for(int i = 0; i < oldRoom.getRowNum(); i++){
            for(int j = 0; j < oldRoom.getColumnNum(); j++){
                if(room.getSeat(i, j).getStatus() == SeatStatus.Removed){ newRoom[i * oldRoom.getColumnNum() + j] = 2; }
                else { newRoom[i * oldRoom.getColumnNum() + j] = 0; }
            }
        }
        return newRoom;
    }

    Room arrayToRoom(int[] solvedRoom){
        Room newRoom = oldRoom;
        for(int i = 0; i < oldRoom.getRowNum(); i++){
            for(int j = 0; j < oldRoom.getColumnNum(); j++){
                if(newRoom.getSeat(i, j).getStatus() == SeatStatus.Removed){ /* Nothing to do */ }
                else if(solvedRoom[i * oldRoom.getColumnNum() + j] == 1){ newRoom.getSeat(i, j).setStatus(SeatStatus.Taken); }
                else { newRoom.getSeat(i, j).setStatus(SeatStatus.Empty); }
            }
        }
        return newRoom;
    }

    public int[] convertIntegers(ArrayList<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    ArrayList<Integer> placeOrder(Room room){
        ArrayList<Integer> place = new ArrayList<Integer>();

        for(int i = 0; i < oldRoom.getRowNum(); i++){
            int count = 0;
            for(int j = 0; j < oldRoom.getColumnNum(); j++){
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

    public Grid roomToView(Room room){
        Grid grid = roomSpreadSheetView.getGrid();
        ArrayList<Integer> places = placeOrder(room);

        for(int i = 0; i < oldRoom.getRowNum(); i++){
            for(int j = 0; j < oldRoom.getColumnNum(); j++){
                if(room.getSeat(i, j).getStatus() != SeatStatus.Removed){
                    grid.getRows().get(i).get(j).setStyle(CellStyles.NORMAL_CELL_STYLE);
                    grid.getRows().get(i).get(j).setItem("");
                }
            }
        }

        for(int i = 0; i < oldRoom.getRowNum(); i++){
            int count = 0;
            for(int j = 0; j < oldRoom.getColumnNum(); j++){
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

    private int[] mapToArray2(Map<Integer, Integer> map){
        int[] arr = roomToArray(oldRoom);
        int counter = -1;
        int[][] twoDim = new int[oldRoom.getRowNum()][oldRoom.getColumnNum()];

        for(int i = 0; i < arr.length; i++){
            if (i % oldRoom.getColumnNum() == 0) counter++;
            twoDim[counter][i % oldRoom.getColumnNum()] = arr[i];
        }

        Map<Integer, Pair<Integer, Integer>> starterPoints = new HashMap();
        int prev = 2;
        int index = 0;

        for(int i = 0; i < oldRoom.getRowNum(); i++){
            for(int j = 0; j < oldRoom.getColumnNum(); j++){
                if (prev != 0 && twoDim[i][j] == 0) { starterPoints.put(index, new Pair<Integer, Integer>(i, j)); index++; }
                prev = twoDim[i][j];
            }
            prev = 2;
        }

        ArrayList<Integer> correctList = new ArrayList<Integer>();
        int e = 0;

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            correctList.add(entry.getValue());
        }

        Collections.sort(correctList);

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            for(int i = 0; i < people[entry.getKey()]; i++){
                Pair<Integer, Integer> cords = starterPoints.get(correctList.get(entry.getKey()));
                twoDim[cords.getKey()][cords.getValue()] = 1;
                starterPoints.put(correctList.get(entry.getKey()), new Pair<Integer, Integer>(cords.getKey(), cords.getValue()+1));
            }
        }

        for(int i = 0; i < oldRoom.getRowNum(); i++) {
            for (int j = 0; j < oldRoom.getColumnNum(); j++) {
                arr[i * oldRoom.getColumnNum() + j] = twoDim[i][j] == 2 ? 0 : twoDim[i][j];
            }
        }

        return arr;
    }
}
