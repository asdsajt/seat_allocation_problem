import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Theater;
import view.View;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        View mainView = new View();
        new Controller(mainView);
        Theater th = new Theater("Csiky Gergely");
        System.out.println(th.getId());
    }

    public static void main(String[] args) {
        launch();
    }
}
