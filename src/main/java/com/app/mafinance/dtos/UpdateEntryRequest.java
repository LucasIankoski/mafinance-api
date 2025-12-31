package com.app.mafinance.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateEntryRequest(
		@NotBlank @Size(max = 180) String description,
		Long categoryId,

		@NotNull @DecimalMin("0.01") BigDecimal amount,
		@NotNull LocalDate date
) {
}