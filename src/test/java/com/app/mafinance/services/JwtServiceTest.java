package com.app.mafinance.services;

import com.app.mafinance.security.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

	@Test
	void generateAndParse_shouldReturnClaims_withIssuerSubjectAndRole() {
		var props = new JwtProperties(
				"mafinance",
				"0123456789abcdef0123456789abcdef",
				10
		);
		var service = new JwtService(props);

		String token = service.generateToken("lucas@teste.com", Map.of("role", "USER"));
		Jws<Claims> parsed = service.parse(token);

		assertEquals("mafinance", parsed.getPayload().getIssuer());
		assertEquals("lucas@teste.com", parsed.getPayload().getSubject());
		assertEquals("USER", parsed.getPayload().get("role", String.class));

		assertNotNull(parsed.getPayload().getExpiration());
		assertTrue(parsed.getPayload().getExpiration().toInstant().isAfter(Instant.now()));
	}

	@Test
	void constructor_shouldFail_whenSecretTooShort() {
		var props = new JwtProperties("mafinance", "short-secret", 10);
		var ex = assertThrows(IllegalStateException.class, () -> new JwtService(props));
		assertTrue(ex.getMessage().contains("JWT secret too short."));
	}
}
