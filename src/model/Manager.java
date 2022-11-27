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
public class Manager extends User implements Serializable {
    private boolean isAdmin;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Nullable
    private byte[] educCertificate;
    @ManyToMany
    private List<Trip> myTrips;

    public Manager(String username, String email, String password, String name, String surname,
                   String address, String phoneNum, LocalDate birthDate, boolean isAdmin,
                   byte[] educCertificate) {
        super(username, email, password, name, surname, address, phoneNum, birthDate);
        this.educCertificate = educCertificate;
        this.isAdmin = isAdmin;

    }

    public Manager (String name, String surname, String username, String email, String password) {
        super(password, name, surname, username, email);
    }
}
