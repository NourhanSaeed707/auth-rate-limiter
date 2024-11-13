package com.example.authentication.Entity;
import jakarta.persistence.*;

@Entity(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private RoleEnum name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
