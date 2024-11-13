package com.example.authentication.model;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    private int status;
    private String token;
    private String message;
}
