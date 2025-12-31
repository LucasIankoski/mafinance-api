package com.app.mafinance.dtos;

import com.app.mafinance.model.enums.EntryType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEntryRequest(
		@NotNull EntryType type,
		@NotBlank @Size(max = 180) String description,
		Long categoryId,

		@NotNull @DecimalMin("0.01") BigDecimal amount,
		@NotNull LocalDate date,

		Boolean paid
) {
}