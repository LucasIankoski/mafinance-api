package com.app.mafinance.services;

import com.app.mafinance.model.AppUser;
import com.app.mafinance.repositories.AppUserRepository;
import com.app.mafinance.dtos.RegisterRequest;
import com.app.mafinance.dtos.RegisterResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserService {

	private final AppUserRepository repo;
	private final PasswordEncoder passwordEncoder;

	public AppUserService(AppUserRepository repo, PasswordEncoder passwordEncoder) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public RegisterResponse register(RegisterRequest req) {
		var email = req.email().trim().toLowerCase();

		if (repo.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException("Email is already in use.");
		}

		var user = new AppUser();
		user.setEmail(email);
		user.setName(req.name().trim());
		user.setPasswordHash(passwordEncoder.encode(req.password()));
		user.setRole("USER");
		user.setEnabled(true);

		var saved = repo.save(user);

		return new RegisterResponse(
				saved.getId(),
				saved.getEmail(),
				saved.getName(),
				saved.getRole(),
				saved.isEnabled()
		);
	}
}
