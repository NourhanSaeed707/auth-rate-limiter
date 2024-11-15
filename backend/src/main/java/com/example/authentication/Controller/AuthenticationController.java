package com.example.authentication.Controller;
import com.example.authentication.Service.AuthenticationService;
import com.example.authentication.DTOs.AuthenticationRequestDTO;
import com.example.authentication.DTOs.AuthenticationResponseDTO;
import com.example.authentication.DTOs.RegisterDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterDTO request, HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.register(request, response));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate (@RequestBody AuthenticationRequestDTO request, HttpServletResponse response) {
        AuthenticationResponseDTO authenticationResponse = authenticationService.authenticate(request,response);
        return ResponseEntity.ok(authenticationResponse);
    }
//    @GetMapping("/user")
//    public ResponseEntity<Optional<UserEntity>> getUserInfo() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Optional<UserEntity> user = userRepository.findByEmail(userDetails.getUsername());
//        System.out.println(user);
//        return ResponseEntity.ok(user);
//
//    }
}
