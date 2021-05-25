package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;
import org.controlsfx.control.spreadsheet.SpreadsheetView;


public class View extends Stage {

    private BorderPane mainBorderPane;

    @Getter
    private SpreadsheetView roomSpreadSheetView;

    @Getter
    private Button addNewTheaterButton, addNewRoomButton, saveCurrentRoomButton, solveButton;

    @Getter
    private Label rowNumberLabel, columnNumberLabel;

    @Getter
    private TextArea groupDefinitionTextArea;

    @Getter
    private ComboBox<String> theaterComboBox, roomComboBox, groupNumberComboBox, solveMethodComboBox;

    @Getter
    private final CheckBox disableSeatsCheckBox;


    public View() {
        roomSpreadSheetView = new SpreadsheetView();

        addNewTheaterButton = new Button("Hozzáadás");
        addNewRoomButton = new Button("Hozzáadás");
        saveCurrentRoomButton = new Button("Mentés");
        solveButton = new Button("Megoldás");

        rowNumberLabel = new Label("15");
        columnNumberLabel = new Label("15");

        groupDefinitionTextArea = new TextArea();

        theaterComboBox = new ComboBox<>();
        roomComboBox = new ComboBox<>();
        groupNumberComboBox = new ComboBox<>();
        solveMethodComboBox = new ComboBox<>();

        disableSeatsCheckBox = new CheckBox();
    }

    public void init() {
        this.setTitle("Projekt alapú szoftverfejlesztés - EV KKL PA");
        Scene scene = new Scene(new Group());
        this.setScene(scene);

        AnchorPane mainLayout = new AnchorPane();
        scene.setRoot(mainLayout);
        mainBorderPane = new BorderPane();
        mainLayout.getChildren().add(mainBorderPane);
        initBorderPaneTop();
        initBorderPaneCenter();
        initBorderPaneRight();
        this.show();
    }

    private VBox twoLineVBox(String firstRow, String secondRow) {
        VBox vBox = new VBox(new Label(firstRow), new Label(secondRow));
        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }

    private void initBorderPaneTop() {
        Label label = new Label("Ülőhelykiosztás probléma megoldó");
        label.setStyle("-fx-font-size: 26px; -fx-font-weight: bold");
        VBox vBox = new VBox(label);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20, 10, 20, 10));

        mainBorderPane.setTop(vBox);
    }

    private void initBorderPaneCenter() {
        roomSpreadSheetView.setContextMenu(null);
        roomSpreadSheetView.setShowColumnHeader(false);
        roomSpreadSheetView.setShowRowHeader(false);
        roomSpreadSheetView.setMinWidth(800);
        roomSpreadSheetView.setMinHeight(800);
        roomSpreadSheetView.setMaxWidth(800);
        roomSpreadSheetView.setMaxHeight(800);
        roomSpreadSheetView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        roomSpreadSheetView.setPadding(new Insets(0,10,10,10));

        mainBorderPane.setCenter(roomSpreadSheetView);
    }

    private void initBorderPaneRight() {
        GridPane rightSideGridPane = new GridPane();
        rightSideGridPane.setPadding(new Insets(0, 0, 10, 0));

        Label label = new Label("Színház adatai");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");
        label.setPadding(new Insets(0, 10, 0, 0));
        rightSideGridPane.add(label, 0, 0, 2, 1);
        rightSideGridPane.add(setComboBoxFor2ColsProperties(theaterComboBox), 0, 1, 2, 1);
        rightSideGridPane.add(setNormalTextProperties("Színház hozzáadás"), 0, 2);
        rightSideGridPane.add(setButtonProperties(addNewTheaterButton), 1, 2);

        rightSideGridPane.add(setH2Properties("Terem adatai"), 0, 3, 2, 1);
        rightSideGridPane.add(setComboBoxFor2ColsProperties(roomComboBox), 0, 4, 2, 1);
        rightSideGridPane.add(setNormalTextProperties("Sorok száma"), 0, 5);
        rightSideGridPane.add(setNumberLabelProperties(rowNumberLabel), 1, 5);
        rightSideGridPane.add(setNormalTextProperties("Oszlopok száma"), 0, 6);
        rightSideGridPane.add(setNumberLabelProperties(columnNumberLabel), 1, 6);
        rightSideGridPane.add(setNormalTextProperties("Terem hozzáadás"), 0, 7);
        rightSideGridPane.add(setButtonProperties(addNewRoomButton), 1, 7);
        rightSideGridPane.add(setNormalTextProperties("Székek eltűntetése"), 0, 8);
        rightSideGridPane.add(disableSeatsCheckBox, 1, 8);
        rightSideGridPane.add(setNormalTextProperties("Jelenlegi terem mentése"), 0, 9);
        rightSideGridPane.add(setButtonProperties(saveCurrentRoomButton), 1, 9);

        rightSideGridPane.add(setH2Properties("Kézi megoldó"), 0, 10, 2, 1);
        rightSideGridPane.add(setNormalTextProperties("Aktuális csoport létszám"), 0, 11);
        rightSideGridPane.add(setComboBoxProperties(groupNumberComboBox), 1, 11);

        rightSideGridPane.add(setH2Properties("Gépi megoldó"), 0, 12, 2, 1);
        rightSideGridPane.add(setNormalTextProperties("Csoportok meghatározása\n(csoportok elválasztása ;-vel)"), 0, 13, 2, 1);
        rightSideGridPane.add(setTextAreaProperties(groupDefinitionTextArea), 0, 14, 2, 1);
        rightSideGridPane.add(setNormalTextProperties("Megoldó kiválasztása"), 0, 15);
        rightSideGridPane.add(setComboBoxProperties(solveMethodComboBox), 1, 15);
        rightSideGridPane.add(addNoteToHbox(setButtonProperties(solveButton)), 0, 16);

        mainBorderPane.setRight(rightSideGridPane);
    }

    private Button setButtonProperties(Button button) {
        button.setMinWidth(100);
        button.setStyle("-fx-font-size: 14px");

        return button;
    }

    private HBox buttonPositioner(Button b1, Button b2) {
        HBox hBox = new HBox(b1, b2);
        double spacing = (groupDefinitionTextArea.getMaxWidth() - b1.getMinWidth() - b2.getMinWidth() - 40) / 2;
        hBox.setPadding(new Insets(5, 10, 5, 10));
        hBox.setSpacing(spacing);
        return hBox;
    }

    private HBox addNoteToHbox(Node node) {
        HBox hBox = new HBox(node);
        hBox.setPadding(new Insets(5, 10, 5, 0));

        return hBox;
    }

    private Node setComboBoxFor2ColsProperties(ComboBox comboBox) {
        comboBox.setPrefWidth(280);
        comboBox.setStyle("-fx-font-size: 13px");
        HBox hBox = new HBox(comboBox);
        hBox.setPrefWidth(300);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(5, 0, 5, 0));
        return addNoteToHbox(hBox);
    }

    private Node setTextAreaProperties(TextArea textArea) {
        textArea.setMaxWidth(300);
        textArea.setMaxHeight(90);
        textArea.setStyle("-fx-font-size: 14px");

        return addNoteToHbox(textArea);
    }

    private Node setComboBoxProperties(ComboBox comboBox) {
        comboBox.setPrefWidth(140);
        comboBox.setStyle("-fx-font-size: 13px");


        return addNoteToHbox(comboBox);
    }

    private Label setNumberLabelProperties(Label label) {
        label.setStyle("-fx-font-size: 14px");
        return label;
    }

    private Label setH2Properties(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");
        label.setPadding(new Insets(20, 10, 0, 0));

        return label;
    }

    private Label setNormalTextProperties(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px");
        label.setPadding(new Insets(6, 10, 6, 0));

        return label;
    }
}
