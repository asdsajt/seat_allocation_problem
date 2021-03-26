package view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class View extends Stage {

    public View() {
        this.setTitle("projekt");
        Scene scene = new Scene(new Group());
        this.setScene(scene);

        AnchorPane mainLayout = new AnchorPane();
        this.setMaxWidth(850);
        this.setMaxHeight(700);
        scene.setRoot(mainLayout);

        Label label = new Label("projekt");
        label.setStyle("-fx-font-size: 22px; -fx-padding: 20px");
        mainLayout.getChildren().add(label);

        this.show();
    }
}
