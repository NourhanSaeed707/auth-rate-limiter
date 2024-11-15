package com.example.authentication.DTOs;
import com.example.authentication.Entity.Gender;
import com.example.authentication.Entity.RoleEnum;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String country;
    @NonNull
    private String city;
    @NonNull
    private Gender gender;

    private String address;
    private RoleEnum role;
}
