package fxcontroller;

import hibernate.Hibernate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
    public ListView<Forum> forumList;
    public Text currentUserHeading;
    public Button deleteAccountBtn;
    public Button logOutBtn;
    public Button accountDetailsBtn;
    public Button viewDetailsVehicleBtn;
    public Button createVehicleBtn;

    private EntityManagerFactory entityManagerFactory;
    private Hibernate hibernate;
    private User currentUser;

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.hibernate = new Hibernate(entityManagerFactory);
        this.currentUser = currentUser;
        currentUserHeading.setText(String.valueOf(currentUser));
        if (currentUser.getClass() == Driver.class) {
            viewDetailsVehicleBtn.setDisable(true);
            createVehicleBtn.setDisable(true);
        }

        fillLists();
    }

    public void setData(EntityManagerFactory entityManagerFactory) {
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

        List<Forum> allForums = hibernate.getAllObjects(new Forum());
        forumList.getItems().clear();
        allForums.forEach(c-> forumList.getItems().add(c));
    }

    public void showDetailsDriver() throws IOException {
        if (driverList.getSelectionModel().getSelectedItem() == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select driver to view details!");
        }
        else {
            newWindowAndWait("../view/accountDetailsDriver.fxml", "Driver");
        }
    }

    public void showDetailsManager() throws IOException {
        if (managerList.getSelectionModel().getSelectedItem() == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select manager to view details!");
        }
        else {
            newWindowAndWait("../view/accountDetailsManager.fxml", "Manager");
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
            newWindowAndWait("../view/detailsVehicle.fxml", "Vehicle");
        }
    }

    public void createTruck() throws IOException {
        newWindowAndWait("../view/detailsVehicle.fxml", "CreateVehicle");
    }

    public void showTripDetails() throws IOException {
        if (tripList.getSelectionModel().getSelectedItem() == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select trip to view details!");
        }
        else {
            newWindowAndWait("../view/detailsTrip.fxml", "Trip");
        }
    }

    public void createTrip() throws IOException {
        newWindowAndWait("../view/detailsTrip.fxml", "CreateTrip");
    }

    public void openForum() throws IOException {
        if (forumList.getSelectionModel().getSelectedItem() == null) {
            utils.fxUtils.generateAlert(Alert.AlertType.INFORMATION, null, "Please select forum to view details!");
        }
        else {
            newWindowAndWait("../view/openForum.fxml", "Forum");
        }
    }

    public void createForum() throws IOException {
        newWindowAndWait("../view/openForum.fxml", "CreateForum");
    }

    public void accountDetails() throws IOException {
        if (currentUser.getClass() == Driver.class) {
            newWindowAndWait("../view/accountDetailsDriver.fxml", "CurrentDriver");
        }
        else {
            newWindowAndWait("../view/accountDetailsManager.fxml", "CurrentManager");
        }
    }

    public void deleteAccount() throws IOException {
        hibernate.removeUser(currentUser);
        loginWindow();
    }

    public void logOut() throws IOException {
        loginWindow();
    }

    private void loginWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/login.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) logOutBtn.getScene().getWindow();
        stage.setTitle("Cargo Transportation System");
        stage.setScene(scene);
        stage.show();
    }

    private void newWindowAndWait(String resource, String object) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(resource));
        Parent parent = fxmlLoader.load();
        if (Objects.equals(object, "Manager")) {
            AccountDetailsManager accountDetailsManager = fxmlLoader.getController();
            accountDetailsManager.setData(entityManagerFactory, currentUser, managerList.getSelectionModel().getSelectedItem());
            accountDetailsManager.updateParentLists(this);
        }
        else if (Objects.equals(object, "Driver")) {
            AccountDetailsDriver accountDetailsDriver = fxmlLoader.getController();
            accountDetailsDriver.setData(entityManagerFactory, currentUser, driverList.getSelectionModel().getSelectedItem());
            accountDetailsDriver.updateParentLists(this);
        }
        else if (Objects.equals(object, "Vehicle")) {
            DetailsVehicle detailsVehicle = fxmlLoader.getController();
            detailsVehicle.setData(entityManagerFactory, truckList.getSelectionModel().getSelectedItem(), currentUser);
            detailsVehicle.updateParentLists(this);
        }
        else if (Objects.equals(object, "CreateVehicle")) {
            DetailsVehicle detailsVehicle = fxmlLoader.getController();
            detailsVehicle.createEntity(entityManagerFactory, true);
            detailsVehicle.updateParentLists(this);
        }
        else if (Objects.equals(object, "Trip")) {
            DetailsTrip detailsTrip = fxmlLoader.getController();
            detailsTrip.setData(entityManagerFactory, tripList.getSelectionModel().getSelectedItem(), currentUser);
            detailsTrip.updateParentLists(this);
        }
        else if (Objects.equals(object, "CreateTrip")) {
            DetailsTrip detailsTrip = fxmlLoader.getController();
            detailsTrip.createEntity(entityManagerFactory, true);
            detailsTrip.updateParentLists(this);
        }
        else if (Objects.equals(object, "Forum")) {
            OpenForum openForum = fxmlLoader.getController();
            openForum.setData(entityManagerFactory, currentUser, forumList.getSelectionModel().getSelectedItem());
            openForum.updateParentLists(this);
        }
        else if (Objects.equals(object, "CreateForum")) {
            OpenForum openForum = fxmlLoader.getController();
            openForum.createEntity(entityManagerFactory, true);
            openForum.updateParentLists(this);
        }
        else if (Objects.equals(object, "CurrentManager")) {
            AccountDetailsManager accountDetailsManager = fxmlLoader.getController();
            accountDetailsManager.setData(entityManagerFactory, currentUser, (Manager) currentUser);
            accountDetailsManager.updateParentLists(this);
        }
        else if (Objects.equals(object, "CurrentDriver")) {
            AccountDetailsDriver accountDetailsDriver = fxmlLoader.getController();
            accountDetailsDriver.setData(entityManagerFactory, currentUser, (Driver) currentUser);
            accountDetailsDriver.updateParentLists(this);
        }
        else if (Objects.equals(object, "Stats")) {
            Stats stats = fxmlLoader.getController();
            stats.setData(entityManagerFactory);
//            stats.updateParentLists(this);
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initOwner(allTabs.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Cargo Transportation System");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void showStats() throws IOException {
        newWindowAndWait("../view/stats.fxml", "Stats");
    }
}
