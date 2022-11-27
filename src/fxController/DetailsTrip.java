package fxController;

import hibernate.Hibernate;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class DetailsTrip {
    public TextField titleField;
    public TextField departureField;
    public TextField stopsField;
    public TextField destinationField;
    public DatePicker dateField;
    public ChoiceBox<TripStatus> statusField;
    public ChoiceBox<Driver> driverField;
    public ChoiceBox<Vehicle> truckField;
    public ChoiceBox<Manager> responsibleField;
    public Button createBtn;
    public Button saveBtn;
    public Button deleteTripBtn;

    private EntityManagerFactory entityManagerFactory;
    private Trip selectedTrip;
    private Hibernate hibernate;
    private Main main;

    public void createEntity(EntityManagerFactory entityManagerFactory, boolean create) {
        this.entityManagerFactory = entityManagerFactory;
        this.hibernate = new Hibernate(entityManagerFactory);
        if (create) {
            createBtn.setVisible(true);
            saveBtn.setVisible(false);
            deleteTripBtn.setVisible(false);
        }
        setChoiceBoxes();
    }

    public void setData(EntityManagerFactory entityManagerFactory, Trip selectedTrip) {
        this.entityManagerFactory = entityManagerFactory;
        this.selectedTrip = selectedTrip;
        this.hibernate = new Hibernate(entityManagerFactory);

        setChoiceBoxes();
        Trip trip = (Trip) hibernate.findById(selectedTrip, selectedTrip.getId());
        fillFields(trip);
    }

    private void fillFields(Trip trip) {
        titleField.setText(trip.getTitle());
        departureField.setText(trip.getDeparture());
        stopsField.setText(trip.getStops());
        destinationField.setText(trip.getDestination());
        dateField.setValue(trip.getDate());
        statusField.setValue(trip.getStatus());
        driverField.setValue(trip.getDriver());
        truckField.setValue(trip.getTruck());
        responsibleField.setValue(trip.getResponsible()); //TODO make it a list
    }

    public void updateParentLists(Main main) {
        this.main = main;
    }

    public void save() {
        Trip trip = (Trip) hibernate.findById(selectedTrip, selectedTrip.getId());
        trip.setTitle(titleField.getText());
        trip.setDeparture(departureField.getText());
        trip.setStops(stopsField.getText());
        trip.setDestination(destinationField.getText());
        trip.setDate(dateField.getValue());
        trip.setStatus(statusField.getValue());
        trip.setDriver(driverField.getValue());
        trip.setTruck(truckField.getValue());
        trip.setResponsible(responsibleField.getValue()); //TODO check when making a list
        hibernate.updateTrip(trip);
        main.setData(entityManagerFactory, trip);
    }

    public void deleteTrip() {
        Trip trip = (Trip) hibernate.findById(selectedTrip, selectedTrip.getId());
        hibernate.removeTrip(trip);
        main.setData(entityManagerFactory, trip);
        Stage stage = (Stage) deleteTripBtn.getScene().getWindow();
        stage.close();
    }

    public void create() {
        this.hibernate = new Hibernate(entityManagerFactory);
        Trip trip = new Trip(titleField.getText(), departureField.getText(), stopsField.getText(),
                destinationField.getText(), dateField.getValue(), statusField.getValue(),
                driverField.getValue(), truckField.getValue(), responsibleField.getValue()); //TODO responsible list
        hibernate.createTrip(trip);
        main.setData(entityManagerFactory, trip);
        Stage stage = (Stage) createBtn.getScene().getWindow();
        stage.close();
    }

    public void setChoiceBoxes() {
        for (TripStatus s:TripStatus.values()) {
            statusField.getItems().add(s);
        }
        List<Vehicle> allTrucks = hibernate.getAllObjects(new Vehicle());
        truckField.getItems().clear();
        allTrucks.forEach(c-> truckField.getItems().add(c));

        List<Driver> allDrivers = hibernate.getAllObjects(new Driver());
        driverField.getItems().clear();
        allDrivers.forEach(c-> driverField.getItems().add(c));

        List<Manager> allManagers = hibernate.getAllObjects(new Manager());
        responsibleField.getItems().clear();
        allManagers.forEach(c-> responsibleField.getItems().add(c));
    }
}
