package com.example.authentication.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity(name = "mother")
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mother {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mobile", nullable = false)
    private String mobile;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "mother")
    private List<Child> children;

}
