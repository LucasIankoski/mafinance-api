package com.app.mafinance.dtos;

import com.app.mafinance.model.enums.EntryType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EntryResponse(
		Long id,
		EntryType type,
		String description,
		BigDecimal amount,
		LocalDate date,
		boolean paid,
		Long categoryId,
		Long groupId,
		Integer installmentNo
) {
}