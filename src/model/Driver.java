package model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver extends User implements Serializable {
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Nullable
    private byte[] medCertificate;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Nullable
    private byte[] driversLicense;
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Trip> myTrips;

    public Driver(String username, String email, String password, String name, String surname,
                   String address, String phoneNum, LocalDate birthDate,
                   byte[] educCertificate, byte[] driversLicense) {
        super(username, email, password, name, surname, address, phoneNum, birthDate);
        this.medCertificate = educCertificate;
        this.driversLicense = driversLicense;

    }

    public Driver(String name, String surname, String username, String email, String password) {
        super(name, surname, username, email, password);
    }
}
