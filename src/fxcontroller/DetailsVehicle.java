package fxcontroller;

import hibernate.Hibernate;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import model.Vehicle;

import javax.persistence.EntityManagerFactory;

public class DetailsVehicle {
    public TextField manufactuterField;
    public TextField modelField;
    public TextField regPlateField;
    public DatePicker yearField;
    public Button deleteTruckBtn;
    public Button createBtn;
    public Button saveBtn;

    private EntityManagerFactory entityManagerFactory;
    private Vehicle selectedVehicle;
    private Hibernate hibernate;
    private Main main;
    private User currentUser;

    public void createEntity(EntityManagerFactory entityManagerFactory, boolean create) {
        this.entityManagerFactory = entityManagerFactory;
        if (create) {
            createBtn.setVisible(true);
            saveBtn.setVisible(false);
            deleteTruckBtn.setVisible(false);
        }
    }

    public void setData(EntityManagerFactory entityManagerFactory, Vehicle selectedVehicle, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.selectedVehicle = selectedVehicle;
        this.hibernate = new Hibernate(entityManagerFactory);
        this.currentUser = currentUser;

        Vehicle vehicle = (Vehicle) hibernate.findById(selectedVehicle, selectedVehicle.getId());
        fillFields(vehicle);
    }

    private void fillFields(Vehicle vehicle) {
        manufactuterField.setText(vehicle.getManufacturer());
        modelField.setText(vehicle.getModel());
        regPlateField.setText(vehicle.getRegPlate());
        yearField.setValue(vehicle.getYear());
    }

    public void updateParentLists(Main main) {
        this.main = main;
    }

    public void save() {
        Vehicle vehicle = (Vehicle) hibernate.findById(selectedVehicle, selectedVehicle.getId());
        vehicle.setManufacturer(manufactuterField.getText());
        vehicle.setModel(modelField.getText());
        vehicle.setRegPlate(regPlateField.getText());
        vehicle.setYear(yearField.getValue());
        hibernate.updateVehicle(vehicle);
        main.setData(entityManagerFactory);
    }

    public void deleteTruck() {
        Vehicle vehicle = (Vehicle) hibernate.findById(selectedVehicle, selectedVehicle.getId());
        hibernate.removeVehicle(vehicle);
        main.setData(entityManagerFactory);
        Stage stage = (Stage) deleteTruckBtn.getScene().getWindow();
        stage.close();
    }

    public void create() {
        this.hibernate = new Hibernate(entityManagerFactory);
        Vehicle vehicle = new Vehicle(manufactuterField.getText(), modelField.getText(),
                regPlateField.getText(), yearField.getValue());
        hibernate.createVehicle(vehicle);
        main.setData(entityManagerFactory);
        Stage stage = (Stage) createBtn.getScene().getWindow();
        stage.close();
    }
}
