package com.app.mafinance.dtos;

import com.app.mafinance.model.enums.EntryType;

public record CategoryResponse(
		Long id,
		EntryType type,
		String name
) {
}