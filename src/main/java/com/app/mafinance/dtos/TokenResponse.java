package com.app.mafinance.dtos;

public record TokenResponse(
		String tokenType,
		String accessToken,
		long expiresInSeconds
) {
}