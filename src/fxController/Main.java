package fxController;

import hibernate.Hibernate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class Main implements Initializable {
    public ListView<Driver> driverList;
    public ListView<Manager> managerList;
    public TabPane allTabs;
    public Button showDetailsBtnDriver;
    public Button showDetailsBtnManager;
    @FXML
    public TableView<Vehicle> truckList;
    @FXML
    public TableColumn<Vehicle, String> manufacturer;
    @FXML
    public TableColumn<Vehicle, String> model;
    @FXML
    public TableColumn<Vehicle, String> regPlate;
    @FXML
    public TableColumn<Vehicle, LocalDate> year;
    @FXML
    public TableView<Trip> tripList;
    @FXML
    public TableColumn<Trip, String> title;
    @FXML
    public TableColumn<Trip, String> departure;
    @FXML
    public TableColumn<Trip, String> stops;
    @FXML
    public TableColumn<Trip, String> destination;
    @FXML
    public TableColumn<Trip, LocalDate> date;
    @FXML
    public TableColumn<Trip, TripStatus> status;
    @FXML
    public TableColumn<Trip, Driver> driver;
    @FXML
    public TableColumn<Trip, Vehicle> truck;
    @FXML
    public TableColumn<Trip, Manager> responsible;

    private EntityManagerFactory entityManagerFactory;
    private Hibernate hibernate;
    private User currentUser;

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.hibernate = new Hibernate(entityManagerFactory);
        this.currentUser = currentUser;

        fillLists();
    }

    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.hibernate = new Hibernate(entityManagerFactory);

        fillLists();
    }

    public void setData(EntityManagerFactory entityManagerFactory, Trip trip) {
        this.entityManagerFactory = entityManagerFactory;
        this.hibernate = new Hibernate(entityManagerFactory);

        fillLists();
    }

    public void fillLists() {
        List<Driver> allDrivers = hibernate.getAllObjects(new Driver());
        driverList.getItems().clear();
        allDrivers.forEach(c-> driverList.getItems().add(c));

        List<Manager> allManagers = hibernate.getAllObjects(new Manager());
        managerList.getItems().clear();
        allManagers.forEach(c-> managerList.getItems().add(c));

        manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        regPlate.setCellValueFactory(new PropertyValueFactory<>("regPlate"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));

        List<Vehicle> allTrucks = hibernate.getAllObjects(new Vehicle());
        truckList.getItems().clear();
        allTrucks.forEach(c-> truckList.getItems().add(c));

        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        departure.setCellValueFactory(new PropertyValueFactory<>("departure"));
        stops.setCellValueFactory(new PropertyValueFactory<>("stops"));
        destination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        driver.setCellValueFactory(new PropertyValueFactory<>("driver"));
        truck.setCellValueFactory(new PropertyValueFactory<>("truck"));
        responsible.setCellValueFactory(new PropertyValueFactory<>("responsible"));

        List<Trip> allTrips = hibernate.getAllObjects(new Trip());
        tripList.getItems().clear();
        allTrips.forEach(c-> tripList.getItems().add(c));
    }

    public void showDetailsDriver() throws IOException {
        if (driverList.getSelectionModel().getSelectedItem() == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select driver to view details!");
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/accountDetailsDriver.fxml"));
            Parent parent = fxmlLoader.load();
            AccountDetailsDriver accountDetailsDriver = fxmlLoader.getController();
            accountDetailsDriver.setData(entityManagerFactory, currentUser, driverList.getSelectionModel().getSelectedItem());
            accountDetailsDriver.updateParentLists(this);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(driverList.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Cargo Transportation System");
            stage.setScene(scene);
            stage.showAndWait();
        }
    }

    public void showDetailsManager() throws IOException {
        if (managerList.getSelectionModel().getSelectedItem() == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select manager to view details!");
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/accountDetailsManager.fxml"));
            Parent parent = fxmlLoader.load();
            AccountDetailsManager accountDetailsManager = fxmlLoader.getController();
            accountDetailsManager.setData(entityManagerFactory, currentUser, managerList.getSelectionModel().getSelectedItem());
            accountDetailsManager.updateParentLists(this);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(managerList.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Cargo Transportation System");
            stage.setScene(scene);
            stage.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void driverClick() {
        showDetailsBtnDriver.setDisable(false);
        showDetailsBtnManager.setDisable(true);
    }

    public void managerClick() {
        showDetailsBtnManager.setDisable(false);
        showDetailsBtnDriver.setDisable(true);
    }

    public void showTruckDetails() throws IOException {
        if (truckList.getSelectionModel().getSelectedItem() == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select vehicle to view details!");
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/detailsVehicle.fxml"));
            Parent parent = fxmlLoader.load();
            DetailsVehicle detailsVehicle = fxmlLoader.getController();
            detailsVehicle.setData(entityManagerFactory, truckList.getSelectionModel().getSelectedItem());
            detailsVehicle.updateParentLists(this);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(truckList.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Cargo Transportation System");
            stage.setScene(scene);
            stage.showAndWait();
        }
    }

    public void createTruck() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/detailsVehicle.fxml"));
        Parent parent = fxmlLoader.load();
        DetailsVehicle detailsVehicle = fxmlLoader.getController();
        detailsVehicle.createEntity(entityManagerFactory, true);
        detailsVehicle.updateParentLists(this);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initOwner(truckList.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Cargo Transportation System");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void showTripDetails() throws IOException {
        if (tripList.getSelectionModel().getSelectedItem() == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select trip to view details!");
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/detailsTrip.fxml"));
            Parent parent = fxmlLoader.load();
            DetailsTrip detailsTrip = fxmlLoader.getController();
            detailsTrip.setData(entityManagerFactory, tripList.getSelectionModel().getSelectedItem());
            detailsTrip.updateParentLists(this);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(tripList.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Cargo Transportation System");
            stage.setScene(scene);
            stage.showAndWait();
        }
    }

    public void createTrip() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/detailsTrip.fxml"));
        Parent parent = fxmlLoader.load();
        DetailsTrip detailsTrip = fxmlLoader.getController();
        detailsTrip.createEntity(entityManagerFactory, true);
        detailsTrip.updateParentLists(this);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initOwner(tripList.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Cargo Transportation System");
        stage.setScene(scene);
        stage.showAndWait();
    }
}
