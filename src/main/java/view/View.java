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
    private Button importRoomFromDBButon, saveRoomToDBButton, groupDefinitionImportButton, groupDefinitionExportButton, solveButton;

    @Getter
    private TextField rowNumberTextField, columnNumberTextField;

    @Getter
    private TextArea groupDefinitionTextArea;

    @Getter
    private ComboBox<String> groupNumberComboBox, solveMethodComboBox;

    @Getter
    private final CheckBox disableSeatsCheckBox;


    public View() {
        roomSpreadSheetView = new SpreadsheetView();

        importRoomFromDBButon = new Button("Import");
        saveRoomToDBButton = new Button("Mentés");
        groupDefinitionImportButton = new Button();
        groupDefinitionImportButton.setGraphic(twoLineVBox("Csoport definíció", "importálás"));
        groupDefinitionExportButton = new Button();
        groupDefinitionExportButton.setGraphic(twoLineVBox("Csoport definíció", "exportálás"));
        solveButton = new Button("Megoldás");

        rowNumberTextField = new TextField("5");
        columnNumberTextField = new TextField("5");

        groupDefinitionTextArea = new TextArea();

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
        vBox.setPadding(new Insets(20,10,20,10));

        mainBorderPane.setTop(vBox);
    }

    private void initBorderPaneCenter() {
        roomSpreadSheetView.setContextMenu(null);
        roomSpreadSheetView.setShowColumnHeader(false);
        roomSpreadSheetView.setShowRowHeader(false);
        roomSpreadSheetView.setMinWidth(700);
        roomSpreadSheetView.setMinHeight(700);
        roomSpreadSheetView.setMaxWidth(700);
        roomSpreadSheetView.setMaxHeight(700);
        roomSpreadSheetView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        roomSpreadSheetView.setPadding(new Insets(10));

        mainBorderPane.setCenter(roomSpreadSheetView);
    }

    private void initBorderPaneRight() {
        GridPane rightSideGridPane = new GridPane();
        rightSideGridPane.setPadding(new Insets(0,0,10,0));

        Label label = new Label("Terem adatai");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");
        label.setPadding(new Insets(0, 10, 0, 0));
        rightSideGridPane.add(label, 0, 0, 2,1);
        rightSideGridPane.add(setNormalTextProperties("Sorok száma"), 0, 1);
        rightSideGridPane.add(setNormalTextProperties("Oszlopok száma"), 0, 2);
        rightSideGridPane.add(setNormalTextProperties("Székek eltűntetése"), 0, 3);
        rightSideGridPane.add(setNormalTextProperties("Előző terem importálása"), 0, 4);
        rightSideGridPane.add(setNormalTextProperties("Jelenlegi terem mentése"), 0, 5);
        rightSideGridPane.add(setNumberTextFieldProperties(rowNumberTextField), 1, 1);
        rightSideGridPane.add(setNumberTextFieldProperties(columnNumberTextField), 1, 2);
        rightSideGridPane.add(disableSeatsCheckBox, 1, 3);
        rightSideGridPane.add(setButtonProperties(importRoomFromDBButon), 1, 4);
        rightSideGridPane.add(setButtonProperties(saveRoomToDBButton), 1, 5);

        rightSideGridPane.add(setH2Properties("Kézi megoldó"), 0, 6, 2, 1);
        rightSideGridPane.add(setNormalTextProperties("Aktuális csoport létszám"), 0, 7);
        rightSideGridPane.add(setComboBoxProperties(groupNumberComboBox), 1, 7);

        rightSideGridPane.add(setH2Properties("Gépi megoldó"), 0, 8, 2, 1);
        rightSideGridPane.add(setNormalTextProperties("Csoportok meghatározása\n(csoportok elválasztása ;-vel)"), 0, 9, 2, 1);
        rightSideGridPane.add(setTextAreaProperties(groupDefinitionTextArea), 0, 10, 2, 1);
        rightSideGridPane.add(buttonPositioner(setButtonProperties(groupDefinitionImportButton), setButtonProperties(groupDefinitionExportButton)), 0, 11, 2, 1);
        rightSideGridPane.add(setNormalTextProperties("Megoldó kiválasztása"), 0, 12);
        rightSideGridPane.add(setComboBoxProperties(solveMethodComboBox), 1, 12);
        rightSideGridPane.add(addNoteToHbox(setButtonProperties(solveButton)), 0, 13);

        mainBorderPane.setRight(rightSideGridPane);
    }

    private Button setButtonProperties(Button button) {
        button.setMinWidth(100);
        button.setStyle("-fx-font-size: 14px");

        return button;
    }

    private HBox buttonPositioner (Button b1, Button b2) {
        HBox hBox = new HBox(b1, b2);
        double spacing = (groupDefinitionTextArea.getMaxWidth() - b1.getMinWidth() - b2.getMinWidth() - 40) / 2;
        hBox.setPadding(new Insets(5,10,5,10));
        hBox.setSpacing(spacing);
        return hBox;
    }

    private HBox addNoteToHbox(Node node) {
        HBox hBox = new HBox(node);
        hBox.setPadding(new Insets(5,10,5,0));

        return hBox;
    }

    private Node setTextAreaProperties(TextArea textArea) {
        textArea.setMaxWidth(300);
        textArea.setMaxHeight(170);
        textArea.setStyle("-fx-font-size: 14px");

        return addNoteToHbox(textArea);
    }

    private Node setComboBoxProperties(ComboBox comboBox) {
        comboBox.setPrefWidth(130);
        comboBox.setStyle("-fx-font-size: 14px");


        return addNoteToHbox(comboBox);
    }

    private TextField setNumberTextFieldProperties(TextField textField) {
        textField.setMaxWidth(100);
        textField.setStyle("-fx-font-size: 14px");
        return textField;
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
        label.setPadding(new Insets(6,10,6,0));

        return label;
    }
}
