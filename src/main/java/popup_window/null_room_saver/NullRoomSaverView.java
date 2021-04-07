package popup_window.null_room_saver;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;

public class NullRoomSaverView extends Stage {

    @Getter
    private TextField roomNameTextField;
    @Getter
    private Label rowNumberLabel, columnNumberLabel;
    @Getter
    private Button addButton, backButton;
    @Getter
    private ComboBox<String> theaterComboBox;

    private GridPane mainGridPane;

    public NullRoomSaverView() {
        addButton = new Button("Mentés");
        backButton = new Button("Vissza");

        theaterComboBox = new ComboBox<>();

        roomNameTextField = new TextField();
    }

    public void init(int rowNumber, int colNumber) {
        this.setTitle("Terem hozzáadása és mentése");
        Scene scene = new Scene(new Group());
        this.setScene(scene);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(320);
        scene.setRoot(anchorPane);

        mainGridPane = new GridPane();
        mainGridPane.setPrefWidth(320);
        mainGridPane.setPadding(new Insets(0,10, 0, 10));

        mainGridPane.add(titleCreator(), 0, 0, 2, 1);
        initLabels();

        mainGridPane.add(styleToComboBox(theaterComboBox), 1,1);
        mainGridPane.add(styleToTextField(roomNameTextField), 1,2);
        rowNumberLabel = new Label(rowNumber + "");
        mainGridPane.add(styleToLabel(rowNumberLabel), 1,3);
        columnNumberLabel = new Label(colNumber + "");
        mainGridPane.add(styleToLabel(columnNumberLabel), 1,4);
        mainGridPane.add(buttons(), 0,5,2,1);
        anchorPane.getChildren().add(mainGridPane);
        this.show();
    }

    private Node styleToLabel(Label label) {
        label.setStyle("-fx-font-size: 14px");
        label.setPrefWidth(200);
        label.setPadding(new Insets(6, 0, 6, 0));

        return label;
    }


    private Node styleToComboBox(ComboBox comboBox) {
        comboBox.setStyle("-fx-font-size: 14px;");
        comboBox.setMaxWidth(200);
        return comboBox;
    }

    private Node styleToTextField(TextField textField) {
        textField.setStyle("-fx-font-size: 14px;");
        textField.setMaxWidth(200);
        return textField;
    }

    private Node buttons() {
        addButton.setStyle("-fx-font-size: 14px;");
        backButton.setStyle("-fx-font-size: 14px;");
        addButton.setMinWidth(100);
        backButton.setMinWidth(100);

        HBox hBox = new HBox(addButton, backButton);
        hBox.setPrefWidth(mainGridPane.getPrefWidth());
        hBox.setPadding(new Insets(20,0,20,0));
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }

    private void initLabels() {
        mainGridPane.add(setNormalTextProperties("Színház neve:"), 0, 1);
        mainGridPane.add(setNormalTextProperties("Terem neve:"), 0, 2);
        mainGridPane.add(setNormalTextProperties("Sorok száma:"), 0, 3);
        mainGridPane.add(setNormalTextProperties("Oszlopok száma:"), 0, 4);
    }

    private Label setNormalTextProperties(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px");
        label.setPrefWidth(120);
        label.setPadding(new Insets(6, 0, 6, 0));

        return label;
    }

    private Node titleCreator() {
        Label title = new Label("Új Terem mentése");
        title.setStyle("-fx-font-size: 22px; -fx-alignment: center; -fx-padding: 20 0 20 0; -fx-font-weight: bold");
        title.setPrefWidth(mainGridPane.getPrefWidth());

        return title;
    }
}