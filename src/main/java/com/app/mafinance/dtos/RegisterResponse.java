package com.app.mafinance.dtos;

public record RegisterResponse(
		Long id,
		String email,
		String name,
		String role,
		boolean enabled
) {}
