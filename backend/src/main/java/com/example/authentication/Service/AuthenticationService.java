package com.example.authentication.Service;
import com.example.authentication.Entity.Role;
import com.example.authentication.Entity.UserEntity;
import com.example.authentication.Repository.RoleRepository;
import com.example.authentication.Repository.UserRepository;
import com.example.authentication.model.AuthenticationRequestDTO;
import com.example.authentication.model.AuthenticationResponseDTO;
import com.example.authentication.model.RegisterDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;


    @Transactional
    public AuthenticationResponseDTO register(RegisterDTO request, HttpServletResponse response) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
            return AuthenticationResponseDTO.builder()
                    .message("Email already in use")
                    .status(409)
                    .build();
        }
        Role role = roleRepository.findByName(request.getRole());
        var user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .country(request.getCountry())
                .city(request.getCity())
                .gender(request.getGender())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
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

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request, HttpServletResponse response) {
//        if (rateLimiterService.isRateLimited()) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            return AuthenticationResponse.builder().message("Too many login requests").status(429).build();
//        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var jwtToken = jwtService.generateToken(user);
            ResponseCookie cookie = ResponseCookie.from("token", jwtToken)
                    .httpOnly(true)
                    .secure(false) // Set to true in production
                    .path("/")
                    .maxAge(86400) // 1 day
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return AuthenticationResponseDTO.builder().token(jwtToken).status(200).build();
        } catch (BadCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return AuthenticationResponseDTO.builder().message("Invalid email or password").status(401).build();
        } catch (UsernameNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return AuthenticationResponseDTO.builder().message("User not found").build();
        }

    }
}
