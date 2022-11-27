package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trip implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String departure;
    private String stops;
    private String destination;
    private LocalDate date;
    private TripStatus status;
    @ManyToOne
    private Driver driver;
    @ManyToOne
    private Vehicle truck;
//    @ManyToMany(mappedBy = "myTrips", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ManyToOne //TODO make it a list
    private Manager responsible;
//    private List<Manager> responsible;

    public Trip(String title, String departure, String stops, String destination, LocalDate date,
                TripStatus status, Driver driver, Vehicle truck, Manager responsible) { //TODO change rep to list
        this.title = title;
        this.departure = departure;
        this.stops = stops;
        this.destination = destination;
        this.date = date;
        this.status = status;
        this.driver = driver;
        this.truck = truck;
        this.responsible = responsible;
    }

    @Override
    public String toString() {
        return title + " " + departure + " " + stops + " " + destination +
                " " + date + " " + status + " " + driver + " " + truck + " " + responsible;
    }
}
