package com.app.mafinance.infra.errors;

import java.time.OffsetDateTime;

public record ApiErrorResponse(
		OffsetDateTime timestamp,
		int status,
		String error,
		String message,
		String path
) {
}