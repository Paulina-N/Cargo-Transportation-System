package utils;

import javafx.scene.control.Alert;

public class fxUtils {
    public static void generateAlert(Alert.AlertType type, String header, String body) {
        Alert alert = new Alert(type);
        alert.setTitle("Cargo Transportation System");
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
    }
}
