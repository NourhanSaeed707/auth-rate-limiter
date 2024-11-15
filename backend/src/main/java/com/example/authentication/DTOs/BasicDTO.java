package com.example.authentication.DTOs;
import com.example.authentication.Entity.Gender;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicDTO {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String country;
    private String city;
    private Gender gender;
}
