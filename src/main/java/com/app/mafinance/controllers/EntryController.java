package com.app.mafinance.controllers;


import com.app.mafinance.dtos.CreateInstallmentExpenseRequest;
import com.app.mafinance.dtos.CreateRecurringIncomeRequest;
import com.app.mafinance.dtos.EntryResponse;
import com.app.mafinance.services.EntryService;
import com.app.mafinance.services.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/entries")
public class EntryController {

	private final EntryService entryService;
	private final CurrentUserService currentUserService;

	public EntryController(EntryService entryService, CurrentUserService currentUserService) {
		this.entryService = entryService;
		this.currentUserService = currentUserService;
	}

	@PostMapping("/installments")
	public ResponseEntity<List<EntryResponse>> createInstallments(
			Authentication auth,
			@Valid @RequestBody CreateInstallmentExpenseRequest req
	) {
		Long userId = currentUserService.requireUserId(auth);

		var entries = entryService.createInstallmentExpense(userId, req).stream()
				.map(entryResponse -> new EntryResponse(
						entryResponse.getId(), entryResponse.getEntryType(), entryResponse.getDescription(), entryResponse.getAmount(), entryResponse.getEntryDate(),
						entryResponse.isPaid(), entryResponse.getCategoryId(), entryResponse.getGroupId(), entryResponse.getInstallmentNo()
				))
				.toList();

		return ResponseEntity.ok(entries);
	}

	@PostMapping("/recurring-income")
	public ResponseEntity<List<EntryResponse>> createRecurringIncome(
			Authentication auth,
			@Valid @RequestBody CreateRecurringIncomeRequest req
	) {
		Long userId = currentUserService.requireUserId(auth);

		var entries = entryService.createRecurringIncome(userId, req).stream()
				.map(e -> new EntryResponse(
						e.getId(), e.getEntryType(), e.getDescription(), e.getAmount(), e.getEntryDate(),
						e.isPaid(), e.getCategoryId(), e.getGroupId(), e.getInstallmentNo()
				))
				.toList();

		return ResponseEntity.ok(entries);
	}

	@GetMapping
	public ResponseEntity<List<EntryResponse>> listByMonth(
			Authentication auth,
			@RequestParam("month") String month
	) {
		Long userId = currentUserService.requireUserId(auth);
		YearMonth yearAndMonth = YearMonth.parse(month);

		var entries = entryService.listByMonth(userId, yearAndMonth).stream()
				.map(entryResponse -> new EntryResponse(
						entryResponse.getId(), entryResponse.getEntryType(), entryResponse.getDescription(), entryResponse.getAmount(), entryResponse.getEntryDate(),
						entryResponse.isPaid(), entryResponse.getCategoryId(), entryResponse.getGroupId(), entryResponse.getInstallmentNo()
				))
				.toList();

		return ResponseEntity.ok(entries);
	}
}
