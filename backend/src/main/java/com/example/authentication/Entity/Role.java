package com.example.authentication.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "roles")
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
