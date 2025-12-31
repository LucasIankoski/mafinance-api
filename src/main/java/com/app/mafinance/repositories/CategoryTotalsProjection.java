package com.app.mafinance.repositories;

import com.app.mafinance.model.enums.EntryType;

import java.math.BigDecimal;

public interface CategoryTotalsProjection {
	EntryType getType();
	Long getCategoryId();
	String getCategoryName();
	BigDecimal getTotal();
}