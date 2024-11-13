package com.example.authentication.Service;
import com.example.authentication.Repository.UserRepository;
import com.example.authentication.model.AuthenticationResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthenticationResponseDTO register(RegisterRequest request, HttpServletResponse response) {
        var user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .nationality(request.getNationality())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        ResponseCookie cookie = ResponseCookie.from("token", jwtToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(86400)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }

}
