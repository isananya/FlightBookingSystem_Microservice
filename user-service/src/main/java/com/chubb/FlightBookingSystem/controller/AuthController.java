package com.chubb.FlightBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.chubb.FlightBookingSystem.dto.AuthResponse;
import com.chubb.FlightBookingSystem.dto.LoginRequest;
import com.chubb.FlightBookingSystem.dto.SignupRequest;
import com.chubb.FlightBookingSystem.model.Role;
import com.chubb.FlightBookingSystem.model.User;
import com.chubb.FlightBookingSystem.repository.UserRepository;
import com.chubb.FlightBookingSystem.security.JwtService;
import com.chubb.FlightBookingSystem.security.UserDetailsImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login successful");
    }

}
