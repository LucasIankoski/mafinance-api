package com.app.mafinance.dtos;

import java.math.BigDecimal;
import java.util.List;

public record MonthlyReportResponse(
		String month,
		BigDecimal totalIncome,
		BigDecimal totalExpense,
		BigDecimal balance,
		List<MonthlyCategoryTotalResponse> byCategory
) {
}