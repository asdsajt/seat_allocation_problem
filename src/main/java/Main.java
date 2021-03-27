import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Theater;
import model.utils.input.JsonImporter;
import model.utils.temp.InputData;
import view.View;

public class Main extends Application {

    private static final String filePath = "temp_files/input_1.json";  // TODO: Replace with GUI fileExplorer

    @Override
    public void start(Stage stage) {
        View mainView = new View();
        new Controller(mainView);
        Theater th = new Theater("Csiky Gergely");
        InputData inputData = new InputData();
        JsonImporter jsonImporter = new JsonImporter();
        jsonImporter.importFile(filePath, inputData);
    }

    public static void main(String[] args) {
        launch();
    }
}
