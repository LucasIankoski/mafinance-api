package com.app.mafinance.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
		@NotBlank @Size(max = 80) String name
) {
}