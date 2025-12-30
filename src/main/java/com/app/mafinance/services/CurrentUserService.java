package com.app.mafinance.services;

import com.app.mafinance.model.AppUser;
import com.app.mafinance.repositories.AppUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

	private final AppUserRepository userRepo;

	public CurrentUserService(AppUserRepository userRepo) {
		this.userRepo = userRepo;
	}

	public Long requireUserId(Authentication authentication) {
		String email = authentication.getName();
		return userRepo.findByEmail(email)
				.map(AppUser::getId)
				.orElseThrow(() -> new IllegalStateException("Authenticated user not found in database: " + email));
	}
}

