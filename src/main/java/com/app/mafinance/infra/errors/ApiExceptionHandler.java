package com.app.mafinance.infra.errors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest req) {
		return ResponseEntity.status(404).body(new ApiErrorResponse(
				OffsetDateTime.now(),
				404,
				"Not Found",
				ex.getMessage(),
				req.getRequestURI()
		));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
		return ResponseEntity.badRequest().body(new ApiErrorResponse(
				OffsetDateTime.now(),
				400,
				"Bad Request",
				ex.getMessage(),
				req.getRequestURI()
		));
	}
}