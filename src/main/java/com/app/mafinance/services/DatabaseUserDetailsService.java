package com.app.mafinance.services;

import com.app.mafinance.repositories.AppUserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

	private final AppUserRepository repo;

	public DatabaseUserDetailsService(AppUserRepository repo) {
		this.repo = repo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = repo.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		if (!user.isEnabled()) {
			throw new DisabledException("User is disabled: " + username);
		}

		var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getEmail())
				.password(user.getPasswordHash())
				.authorities(authorities)
				.accountLocked(false)
				.disabled(false)
				.build();
	}
}
