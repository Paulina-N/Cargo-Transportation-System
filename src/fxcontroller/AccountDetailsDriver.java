package fxcontroller;

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
import model.User;

import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Logger;

public class AccountDetailsDriver extends Window {
    public TextField nameField;
    public TextField surnameField;
    public TextField usernameField;
    public TextField emailField;
    public DatePicker birthDateField;
    public TextField phoneNumField;
    public TextField addressField;
    public Button updateDetailsBtn;
    public Button saveBtn;
    public Button deleteUserBtn;
    public ImageView driversLicenseFile;
    public Button uploadDriversLicenseBtn;
    public ImageView medicalFile;
    public Button uploadMedicalFileBtn;
    private byte[] newDriverLicense;
    private byte[] newMedicalFile;

    private boolean image1Exists = false;
    private boolean image2Exists = false;

    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private Hibernate hibernate;
    private Driver selectedUser;
    private Main main;

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser, Driver selectedUser) {
        this.currentUser = currentUser;
        this.entityManagerFactory = entityManagerFactory;
        this.selectedUser = selectedUser;
        this.hibernate = new Hibernate(entityManagerFactory);

        if (currentUser.getClass() == Driver.class && currentUser != selectedUser) {
            updateDetailsBtn.setVisible(false);
            saveBtn.setVisible(false);
            deleteUserBtn.setVisible(false);
        }

        Driver user = (Driver) hibernate.findById(selectedUser, selectedUser.getId());
        fillFields(user);
    }

    private void fillFields(Driver user) {
        nameField.setText(user.getName());
        surnameField.setText(user.getSurname());
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneNumField.setText(user.getPhoneNum());
        birthDateField.setValue(user.getBirthDate());
        addressField.setText(user.getAddress());
        if (user.getDriversLicense() != null) {
            Image file = new Image(new ByteArrayInputStream(user.getDriversLicense()));
            driversLicenseFile.setImage(file);
            image1Exists = true;
        }
        if (user.getMedCertificate() != null) {
            Image file = new Image(new ByteArrayInputStream(user.getMedCertificate()));
            medicalFile.setImage(file);
            image2Exists = true;
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
        uploadDriversLicenseBtn.setVisible(true);
        uploadMedicalFileBtn.setVisible(true);
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
        uploadDriversLicenseBtn.setVisible(false);
        uploadMedicalFileBtn.setVisible(false);
        updateDetailsBtn.setVisible(true);
        saveBtn.setVisible(false);

        Driver user = (Driver) hibernate.findById(selectedUser, selectedUser.getId());
        user.setName(nameField.getText());
        user.setSurname(surnameField.getText());
        user.setUsername(usernameField.getText());
        user.setEmail(emailField.getText());
        user.setPhoneNum(phoneNumField.getText());
        user.setBirthDate(birthDateField.getValue());
        user.setAddress(addressField.getText());
        if (newDriverLicense != null) {
            user.setDriversLicense(newDriverLicense);
            image1Exists = true;
        }
        if (newMedicalFile != null) {
            user.setMedCertificate(newMedicalFile);
            image2Exists = true;
        }
        hibernate.updateUser(user);
        main.setData(entityManagerFactory);
    }

    public void updateParentLists(Main main) {
        this.main = main;
    }

    public void deleteUser() {
        Driver user = (Driver) hibernate.findById(selectedUser, selectedUser.getId());
        hibernate.removeUser(user);
        main.setData(entityManagerFactory);
        Stage stage = (Stage) deleteUserBtn.getScene().getWindow();
        stage.close();
    }

    public void uploadDriversLicense() {
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
                driversLicenseFile.setImage(image);
                driversLicenseFile.setFitWidth(371);
                driversLicenseFile.setFitHeight(68);
                driversLicenseFile.scaleXProperty();
                driversLicenseFile.scaleYProperty();
                driversLicenseFile.setSmooth(true);
                driversLicenseFile.setCache(true);
                FileInputStream fin = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];

                for (int readNum; (readNum = fin.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
                }
                newDriverLicense = bos.toByteArray();
            }

        } catch (IOException ex) {
            Logger.getLogger("ss");
        }
    }

    public void uploadMedicalFile() {
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
            WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
            medicalFile.setImage(image);
            medicalFile.setFitWidth(371);
            medicalFile.setFitHeight(68);
            medicalFile.scaleXProperty();
            medicalFile.scaleYProperty();
            medicalFile.setSmooth(true);
            medicalFile.setCache(true);
            FileInputStream fin = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for (int readNum; (readNum = fin.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            newMedicalFile = bos.toByteArray();

        } catch (IOException ex) {
            Logger.getLogger("ss");
        }
    }

    public void openLicense() throws IOException {
        openImage(image1Exists, driversLicenseFile, "Drivers License");
    }

    public void openMedical() throws IOException {
        openImage(image2Exists, medicalFile, "Medical File");
    }

    private void openImage(boolean exists, ImageView image, String name) throws IOException {
        if (exists) {
            FXMLLoader fxmlLoader = new FXMLLoader(AccountDetailsManager.class.getResource("../view/viewImage.fxml"));
            Parent parent = fxmlLoader.load();
            ViewImage viewImage = fxmlLoader.getController();
            viewImage.setImage(image.getImage());
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(image.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle(name);
            stage.setScene(scene);
            stage.showAndWait();
        }
    }
}
