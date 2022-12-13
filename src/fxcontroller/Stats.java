package fxcontroller;

import hibernate.Hibernate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import model.Trip;
import model.TripStatus;

import javax.persistence.EntityManagerFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Stats {
    @FXML
    public PieChart piechart;
    private int cancelled;
    private int ongoing;
    private int completed;

    private EntityManagerFactory entityManagerFactory;
    private Hibernate hibernate;

    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.hibernate = new Hibernate(entityManagerFactory);

        List<Trip> allTrips = hibernate.getAllObjects(new Trip());

        for (Trip trip : allTrips) {
            if (trip.getStatus() == TripStatus.CANCELED) cancelled++;
            else if (trip.getStatus() == TripStatus.ONGOING) ongoing++;
            else completed++;
        }

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data(TripStatus.CANCELED.toString(), cancelled),
                        new PieChart.Data(TripStatus.ONGOING.toString(), ongoing),
                        new PieChart.Data(TripStatus.COMPLETED.toString(), completed));

        piechart.setData(pieChartData);
    }
}
