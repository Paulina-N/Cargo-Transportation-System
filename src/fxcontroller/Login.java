package fxcontroller;

import hibernate.Hibernate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class Login {
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    public CheckBox managerCheck;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CargoTransportationsSystem");
    Hibernate userhib = new Hibernate(entityManagerFactory);

    public void loginToAccount() throws IOException {
        User user = userhib.getUserByLoginData("username", "password", usernameField.getText(), passwordField.getText(), managerCheck.isSelected());
        if (user != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("../view/main.fxml"));
            Parent parent = fxmlLoader.load();
            Main main = fxmlLoader.getController();
            main.setData(entityManagerFactory, user);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Cargo Transportation System");
            stage.setScene(scene);
            stage.show();
        }
        else {
            utils.fxUtils.generateAlert(Alert.AlertType.ERROR, null, "Wrong login details, try again!");
        }
    }

    public void registerLink() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("../view/registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Cargo Transportation System");
        stage.setScene(scene);
        stage.show();
    }
}
