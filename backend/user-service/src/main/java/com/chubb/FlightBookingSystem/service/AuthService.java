package com.chubb.FlightBookingSystem.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.PasswordChangeRequest;
import com.chubb.FlightBookingSystem.dto.SignupRequest;
import com.chubb.FlightBookingSystem.model.Role;
import com.chubb.FlightBookingSystem.model.User;
import com.chubb.FlightBookingSystem.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepo;
	private final PasswordEncoder encoder;

	public AuthService(UserRepository userRepo, PasswordEncoder encoder) {
		this.userRepo = userRepo;
		this.encoder = encoder;
	}

	public User register(SignupRequest req) {
		if (userRepo.existsByEmail(req.getEmail())) {
			throw new RuntimeException("User already exists");
		}

		User user = new User();
		user.setName(req.getName());
		user.setEmail(req.getEmail());
		user.setPassword(encoder.encode(req.getPassword()));

		Role role = (req.getRole() == null) ? Role.ROLE_USER : req.getRole();
		user.setRole(role);

		return userRepo.save(user);
	}

	public void changePassword(String email, PasswordChangeRequest request) {

		User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Current password is incorrect");
		}

		user.setPassword(encoder.encode(request.getNewPassword()));
		userRepo.save(user);
	}
}
