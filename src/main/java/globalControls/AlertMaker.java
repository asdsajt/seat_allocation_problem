package globalControls;

import javafx.scene.control.Alert;
import javafx.stage.Window;

public class AlertMaker extends Alert {

    private AlertMaker(AlertType alertType, Window owner, String title, String message) {
        super(alertType);
        setHeaderText(null);
        initOwner(owner);
        this.setTitle(title);
        setContentText(message);
        show();
    }

    public static void make(AlertType alertType, Window owner, String title, String message) {
        new AlertMaker(alertType, owner, title, message);
    }

}
