package com.app.mafinance.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateInstallmentExpenseRequest(
		@NotBlank @Size(max = 140) String title,
		@NotBlank @Size(max = 180) String description,
		Long categoryId,

		@NotNull @DecimalMin("0.01") BigDecimal totalAmount,
		@NotNull @Min(2) @Max(120) Integer installments,
		@NotNull LocalDate firstDate
) {
}