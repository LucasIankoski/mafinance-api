package com.app.mafinance.repositories;

import java.math.BigDecimal;

public interface MonthlyTotalsProjection {
	BigDecimal getIncome();
	BigDecimal getExpense();
}