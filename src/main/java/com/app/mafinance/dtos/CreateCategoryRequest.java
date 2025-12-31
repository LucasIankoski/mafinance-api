package com.app.mafinance.dtos;

import com.app.mafinance.model.enums.EntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
		@NotNull EntryType type,
		@NotBlank @Size(max = 80) String name
) {
}