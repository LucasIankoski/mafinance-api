package com.app.mafinance.dtos;

import com.app.mafinance.model.enums.EntryType;

import java.math.BigDecimal;

public record MonthlyCategoryTotalResponse(
		EntryType type,
		Long categoryId,
		String categoryName,
		BigDecimal total
) {
}