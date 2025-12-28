package com.app.mafinance.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mafinance.security.jwt")
public record JwtProperties(
		String issuer,
		String secret,
		long expirationMinutes
) {
}