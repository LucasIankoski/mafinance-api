package com.app.mafinance.controllers;

import com.app.mafinance.dtos.LoginRequest;
import com.app.mafinance.dtos.RegisterRequest;
import com.app.mafinance.dtos.RegisterResponse;
import com.app.mafinance.dtos.TokenResponse;
import com.app.mafinance.security.jwt.JwtProperties;
import com.app.mafinance.services.JwtService;
import com.app.mafinance.repositories.AppUserRepository;
import com.app.mafinance.services.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AppUserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final JwtProperties jwtProperties;
	private final AppUserRepository userRepo;

	public AuthController(
			AppUserService userService,
			AuthenticationManager authenticationManager,
			JwtService jwtService,
			JwtProperties jwtProperties,
			AppUserRepository userRepo
	) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.jwtProperties = jwtProperties;
		this.userRepo = userRepo;
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest req) {
		return ResponseEntity.ok(userService.register(req));
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
		String email = req.email().trim().toLowerCase();

		Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, req.password())
		);

		String role = userRepo.findByEmail(email)
				.map(u -> u.getRole())
				.orElse("USER");

		String token = jwtService.generateToken(
				auth.getName(),
				Map.of("role", role)
		);

		long expiresInSeconds = jwtProperties.expirationMinutes() * 60;

		return ResponseEntity.ok(new TokenResponse("Bearer", token, expiresInSeconds));
	}
}

