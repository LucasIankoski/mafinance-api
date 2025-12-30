package com.app.mafinance.services;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EntryServiceTest {

	@Test
	void splitIntoInstallments_shouldDistributeRemainderToFirstInstallments() {
		var list = EntryService.splitIntoInstallments(new BigDecimal("100.00"), 3);

		assertEquals(new BigDecimal("33.34"), list.get(0));
		assertEquals(new BigDecimal("33.33"), list.get(1));
		assertEquals(new BigDecimal("33.33"), list.get(2));

		var sum = list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		assertEquals(new BigDecimal("100.00"), sum);
	}
}