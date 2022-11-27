package fxController;

import hibernate.Hibernate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Driver;
import model.Manager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Registration implements Initializable {
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField repeatPasswordField;
    @FXML
    public RadioButton driverRadio;
    @FXML
    public RadioButton managerRadio;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CargoTransportationsSystem");
    Hibernate hibernate = new Hibernate(entityManagerFactory);

    public void driverAccount() {
        managerRadio.setSelected(!driverRadio.isSelected());
    }
    public void managerAccount() {
        driverRadio.setSelected(!managerRadio.isSelected());
    }

    public void createNewAccount() throws IOException {
        StringBuilder errors = new StringBuilder();

        if ((nameField.getText().trim().isEmpty()) || (surnameField.getText().trim().isEmpty()) || (usernameField.getText().trim().isEmpty()) ||
                (emailField.getText().trim().isEmpty()) || (passwordField.getText().trim().isEmpty()) || (repeatPasswordField.getText().trim().isEmpty())) {
            errors.append("- Don't leave any blank fields!\n");
        }

        if (hibernate.getUserByUsername(usernameField.getText()) != null) {
            errors.append("- This username is already taken!\n");
        }

        if (hibernate.getUserByEmail(emailField.getText()) != null) {
            errors.append("- This email is already registered!\n");
        }

        if (!Objects.equals(passwordField.getText(), repeatPasswordField.getText())) {
            errors.append("- Passwords don't match!\n");
        }

        if (errors.length() > 0) {
            utils.fxUtils.generateAlert(Alert.AlertType.ERROR, null, errors.toString());
        }
        else {
            if (driverRadio.isSelected()) {
                Driver driver = new Driver(nameField.getText(), surnameField.getText(),
                        usernameField.getText(), emailField.getText(), passwordField.getText());
                hibernate.createUser(driver);
            }
            else if (managerRadio.isSelected()) {
                Manager manager = new Manager(nameField.getText(), surnameField.getText(),
                        usernameField.getText(), emailField.getText(), passwordField.getText());
                hibernate.createUser(manager);
            }

            FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("../view/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Cargo Transportation System");
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        driverAccount();
    }

    public void loginLink() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("../view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Cargo Transportation System");
        stage.setScene(scene);
        stage.show();
    }
}
