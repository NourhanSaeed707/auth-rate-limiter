package com.example.authentication.Service;

import com.example.authentication.Entity.Role;
import com.example.authentication.Entity.RoleEnum;
import com.example.authentication.Entity.UserEntity;
import com.example.authentication.Repository.RoleRepository;
import com.example.authentication.Repository.UserRepository;
import com.example.authentication.DTOs.AuthenticationRequestDTO;
import com.example.authentication.DTOs.AuthenticationResponseDTO;
import com.example.authentication.DTOs.RegisterDTO;
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
        if (isEmailAlreadyInUse(request.getEmail())) {
            return buildErrorResponse(response, HttpServletResponse.SC_CONFLICT, "Email already in use");
        }

        UserEntity user = createUserEntity(request);
        userRepository.save(user);
        createRoleForUser(user, request.getRole());

        String jwtToken = jwtService.generateToken(user);
        addTokenToResponse(response, jwtToken);

        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticateUser(request);
            UserEntity user = getUserByEmail(request.getEmail());

            String jwtToken = jwtService.generateToken(user);
            addTokenToResponse(response, jwtToken);

            return AuthenticationResponseDTO.builder().token(jwtToken).status(200).build();
        } catch (BadCredentialsException e) {
            return buildErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid email or password");
        } catch (UsernameNotFoundException e) {
            return buildErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

    private boolean isEmailAlreadyInUse(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private UserEntity createUserEntity(RegisterDTO request) {
        return UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .country(request.getCountry())
                .city(request.getCity())
                .gender(request.getGender())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(null)
                .build();
    }

    private void createRoleForUser(UserEntity user, RoleEnum roleName) {
        Role role = Role.builder().name(roleName).user(user).build();
        roleRepository.save(role);
    }

    private Authentication authenticateUser(AuthenticationRequestDTO request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }

    private UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private void addTokenToResponse(HttpServletResponse response, String jwtToken) {
        ResponseCookie cookie = ResponseCookie.from("token", jwtToken)
                .httpOnly(true)
                .secure(false) // Set to true in production
                .path("/")
                .maxAge(86400) // 1 day
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private AuthenticationResponseDTO buildErrorResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        return AuthenticationResponseDTO.builder()
                .message(message)
                .status(status)
                .build();
    }
}
