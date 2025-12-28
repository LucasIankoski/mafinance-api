package com.app.mafinance.services;

import com.app.mafinance.security.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

	private final JwtProperties props;
	private final SecretKey key;

	public JwtService(JwtProperties props) {
		this.props = props;

		byte[] secretBytes = props.secret().getBytes(StandardCharsets.UTF_8);
		if (secretBytes.length < 32) {
			throw new IllegalStateException(
					"JWT secret muito curto. Para HS256, use pelo menos 32 bytes (256 bits)."
			);
		}
		this.key = Keys.hmacShaKeyFor(secretBytes);
	}

	public String generateToken(String subject, Map<String, Object> claims) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(props.expirationMinutes() * 60);

		return Jwts.builder()
				.issuer(props.issuer())
				.subject(subject)
				.claims(claims)
				.issuedAt(Date.from(now))
				.expiration(Date.from(exp))
				.signWith(key, Jwts.SIG.HS256)
				.compact();
	}

	public Jws<Claims> parse(String token) throws JwtException {
		return Jwts.parser()
				.verifyWith(key)
				.requireIssuer(props.issuer())
				.build()
				.parseSignedClaims(token);
	}
}
