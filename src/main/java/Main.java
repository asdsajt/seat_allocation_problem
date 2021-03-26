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
    }

    public static void main(String[] args) {
        Theater th = new Theater();
        th.setId(5);
        th.setName("qwe");
        System.out.println(th.getId() + " " + th.getName());
        launch();
    }
}
