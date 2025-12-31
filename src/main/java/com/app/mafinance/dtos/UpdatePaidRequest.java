package com.app.mafinance.dtos;

import jakarta.validation.constraints.NotNull;

public record UpdatePaidRequest(
		@NotNull Boolean paid
) {
}