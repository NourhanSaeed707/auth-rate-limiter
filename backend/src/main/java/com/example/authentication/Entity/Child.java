package com.example.authentication.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "kids")
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @ManyToOne
    @JoinColumn(name = "mother_id")
    private Mother mother;
}
