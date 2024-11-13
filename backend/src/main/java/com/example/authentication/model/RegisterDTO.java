package com.example.authentication.model;
import com.example.authentication.Entity.Gender;
import com.example.authentication.Entity.Role;
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
    private Role role;
}
