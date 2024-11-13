package com.example.authentication.model;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobile;
    private String country;
}
