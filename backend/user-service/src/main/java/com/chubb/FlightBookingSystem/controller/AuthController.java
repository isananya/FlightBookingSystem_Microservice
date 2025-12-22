package com.chubb.FlightBookingSystem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.chubb.FlightBookingSystem.dto.AuthResponse;
import com.chubb.FlightBookingSystem.dto.LoginRequest;
import com.chubb.FlightBookingSystem.dto.PasswordChangeRequest;
import com.chubb.FlightBookingSystem.dto.SignupRequest;
import com.chubb.FlightBookingSystem.model.Role;
import com.chubb.FlightBookingSystem.model.User;
import com.chubb.FlightBookingSystem.repository.UserRepository;
import com.chubb.FlightBookingSystem.security.JwtService;
import com.chubb.FlightBookingSystem.security.UserDetailsImpl;
import com.chubb.FlightBookingSystem.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User with email already exists");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.ROLE_USER);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

        String jwt = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("jwt_token", jwt)
                .httpOnly(true)
                .secure(false) 
                .path("/")
                .maxAge(2 * 60 * 60) // 2 hours
                .build();

        Map<String, Object> response = new HashMap<>();
        response.put("email", request.getEmail());
        String role = user.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");
        response.put("role", role);
        response.put("name", user.getUser().getName());
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
    
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@CookieValue("jwt_token") String token,
            @RequestBody @Valid PasswordChangeRequest dto) {

        String email = jwtService.extractUsername(token);
        authService.changePassword(email, dto);

        return ResponseEntity.noContent().build();
    }


}
