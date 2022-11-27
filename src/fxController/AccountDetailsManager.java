package fxController;

import hibernate.Hibernate;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Driver;
import model.Manager;
import model.User;

import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Logger;

public class AccountDetailsManager extends Window {
    public TextField nameField;
    public TextField surnameField;
    public TextField usernameField;
    public TextField emailField;
    public TextField phoneNumField;
    public DatePicker birthDateField;
    public TextField addressField;
    public Button updateDetailsBtn;
    public Button saveBtn;
    public Button deleteUserBtn;
    public ImageView educCertificateFile;
    public Button uploadCertificateBtn;
    private byte[] newFile;
    private boolean imageExists = false;

    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private Hibernate hibernate;
    private Manager selectedUser;
    private Main main;

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser, Manager selectedUser) {
        this.currentUser = currentUser;
        this.entityManagerFactory = entityManagerFactory;
        this.selectedUser = selectedUser;
        this.hibernate = new Hibernate(entityManagerFactory);

        if (currentUser.getClass() == Driver.class) {
            updateDetailsBtn.setVisible(false);
            saveBtn.setVisible(false);
            deleteUserBtn.setVisible(false);
        }

        Manager user = (Manager) hibernate.findById(selectedUser, selectedUser.getId());
        fillFields(user);
    }

    private void fillFields(Manager user) {
        nameField.setText(user.getName());
        surnameField.setText(user.getSurname());
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneNumField.setText(user.getPhoneNum());
        birthDateField.setValue(user.getBirthDate());
        addressField.setText(user.getAddress());
        if (user.getEducCertificate() != null) {
            Image file = new Image(new ByteArrayInputStream(user.getEducCertificate()));
            educCertificateFile.setImage(file);
            imageExists = true;
        }
    }

    public void updateDetails() {
        nameField.setDisable(false);
        surnameField.setDisable(false);
        usernameField.setDisable(false);
        emailField.setDisable(false);
        phoneNumField.setDisable(false);
        birthDateField.setDisable(false);
        addressField.setDisable(false);
        uploadCertificateBtn.setVisible(true);
        updateDetailsBtn.setVisible(false);
        saveBtn.setVisible(true);
    }

    public void save() {
        nameField.setDisable(true);
        surnameField.setDisable(true);
        usernameField.setDisable(true);
        emailField.setDisable(true);
        phoneNumField.setDisable(true);
        birthDateField.setDisable(true);
        addressField.setDisable(true);
        uploadCertificateBtn.setVisible(false);
        updateDetailsBtn.setVisible(true);
        saveBtn.setVisible(false);

        Manager user = (Manager) hibernate.findById(selectedUser, selectedUser.getId());
        user.setName(nameField.getText());
        user.setSurname(surnameField.getText());
        user.setUsername(usernameField.getText());
        user.setEmail(emailField.getText());
        user.setPhoneNum(phoneNumField.getText());
        user.setBirthDate(birthDateField.getValue());
        user.setAddress(addressField.getText());
        if (newFile != null) {
            user.setEducCertificate(newFile);
            imageExists = true;
        }
        hibernate.updateUser(user);
        main.setData(entityManagerFactory, currentUser);
    }

    public void updateParentLists(Main main) {
        this.main = main;
    }

    public void deleteUser() {
        Manager user = (Manager) hibernate.findById(selectedUser, selectedUser.getId());
        hibernate.removeUser(user);
        main.setData(entityManagerFactory, currentUser);
        Stage stage = (Stage) deleteUserBtn.getScene().getWindow();
        stage.close();
    }

    public void uploadCertificate() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterJPEG
                = new FileChooser.ExtensionFilter("jpeg files (*.jpeg)", "*.jpeg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterJPEG,
                        extFilterPNG);

        File file = fileChooser.showOpenDialog(this);

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            if (bufferedImage == null) {
                utils.fxUtils.generateAlert(Alert.AlertType.ERROR, null, "The selected file is not supported, " +
                        "choose another file!\n(supported files: JPG, JPEG, PNG).");
            } else {
                WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
                educCertificateFile.setImage(image);
                educCertificateFile.setFitWidth(371);
                educCertificateFile.setFitHeight(68);
                educCertificateFile.scaleXProperty();
                educCertificateFile.scaleYProperty();
                educCertificateFile.setSmooth(true);
                educCertificateFile.setCache(true);
                FileInputStream fin = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];

                for (int readNum; (readNum = fin.read(buf)) != -1; ) {
                    bos.write(buf, 0, readNum);
                }
                newFile = bos.toByteArray();
            }
        } catch (IOException ex) {
            Logger.getLogger("ss");
        }
    }

    public void openImage() throws IOException {
        if (imageExists) {
            FXMLLoader fxmlLoader = new FXMLLoader(AccountDetailsManager.class.getResource("../view/viewImage.fxml"));
            Parent parent = fxmlLoader.load();
            ViewImage viewImage = fxmlLoader.getController();
            viewImage.setImage(educCertificateFile.getImage());
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(educCertificateFile.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Education Certificate");
            stage.setScene(scene);
            stage.showAndWait();
        }
    }
}
