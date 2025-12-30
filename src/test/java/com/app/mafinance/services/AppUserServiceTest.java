package com.app.mafinance.services;

import com.app.mafinance.dtos.RegisterRequest;
import com.app.mafinance.model.AppUser;
import com.app.mafinance.repositories.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

	@Mock
	AppUserRepository repo;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	AppUserService service;

	@Test
	void register_shouldCreateUser_withEncodedPassword_andDefaultRoleEnabled() {
		var req = new RegisterRequest("Lucas@Teste.com", " Lucas ", "SenhaForte123");
		when(repo.findByEmail("lucas@teste.com")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("SenhaForte123")).thenReturn("HASHED");
		when(repo.save(any(AppUser.class))).thenAnswer(inv -> {
			AppUser u = inv.getArgument(0);
			try {
				var idField = AppUser.class.getDeclaredField("id");
				idField.setAccessible(true);
				idField.set(u, 1L);
			} catch (Exception ignored) {}
			return u;
		});

		var res = service.register(req);

		assertEquals(1L, res.id());
		assertEquals("lucas@teste.com", res.email());
		assertEquals("Lucas", res.name());
		assertEquals("USER", res.role());
		assertTrue(res.enabled());

		ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
		verify(repo).save(captor.capture());
		AppUser saved = captor.getValue();

		assertEquals("lucas@teste.com", saved.getEmail());
		assertEquals("Lucas", saved.getName());
		assertEquals("HASHED", saved.getPasswordHash());
		assertEquals("USER", saved.getRole());
		assertTrue(saved.isEnabled());
	}

	@Test
	void register_shouldThrow_whenEmailAlreadyExists() {
		var req = new RegisterRequest("lucas@teste.com", "Lucas", "SenhaForte123");
		when(repo.findByEmail("lucas@teste.com")).thenReturn(Optional.of(new AppUser()));

		var ex = assertThrows(IllegalArgumentException.class, () -> service.register(req));
		assertEquals("Email is already in use.", ex.getMessage());

		verify(repo, never()).save(any());
		verify(passwordEncoder, never()).encode(any());
	}
}
