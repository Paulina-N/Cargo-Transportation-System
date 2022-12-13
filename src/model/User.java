package model;

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
@MappedSuperclass
public abstract class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String address;
    private String phoneNum;
    private LocalDate birthDate;

    public User(String name, String surname, String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public User(String username, String email, String password, String name, String surname,
                String address, String phoneNum, LocalDate birthDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phoneNum = phoneNum;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
