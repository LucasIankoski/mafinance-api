package com.app.mafinance.controllers.validator;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

	@GetMapping
	public ResponseEntity<Map<String, Object>> me(Authentication auth) {
		return ResponseEntity.ok(Map.of(
				"username", auth.getName(),
				"authorities", auth.getAuthorities()
		));
	}
}
