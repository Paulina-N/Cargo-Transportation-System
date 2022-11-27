package model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String manufacturer;
    private String model;
    private String regPlate;
    private LocalDate year;
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Trip> trips;

    public Vehicle(String manufacturer, String model, String regPlate, LocalDate year) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.regPlate = regPlate;
        this.year = year;
    }

    @Override
    public String toString() {
        return manufacturer + " " + model + " " + regPlate + " " + year.getYear();
    }
}
