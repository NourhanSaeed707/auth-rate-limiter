package com.example.authentication.Entity;
import jakarta.persistence.*;

@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstName", nullable = false)
    private String firstName;
    @Column(name = "lastName", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Mother mother;
    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private Child child;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Role role;

}
