package com.app.mafinance.controllers;

import com.app.mafinance.dtos.CreateEntryRequest;
import com.app.mafinance.dtos.CreateInstallmentExpenseRequest;
import com.app.mafinance.dtos.CreateRecurringIncomeRequest;
import com.app.mafinance.dtos.EntryResponse;
import com.app.mafinance.dtos.UpdateEntryRequest;
import com.app.mafinance.dtos.UpdatePaidRequest;
import com.app.mafinance.services.EntryService;
import com.app.mafinance.services.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
				.map(entryResponse -> new EntryResponse(
						entryResponse.getId(), entryResponse.getEntryType(), entryResponse.getDescription(), entryResponse.getAmount(), entryResponse.getEntryDate(),
						entryResponse.isPaid(), entryResponse.getCategoryId(), entryResponse.getGroupId(), entryResponse.getInstallmentNo()
				))
				.toList();

		return ResponseEntity.ok(entries);
	}

	@GetMapping
	public ResponseEntity<List<EntryResponse>> listByMonth(
			Authentication auth,
			@RequestParam("month") String month, // YYYY-MM
			@RequestParam(value = "type", required = false) com.app.mafinance.model.enums.EntryType type,
			@RequestParam(value = "paid", required = false) Boolean paid,
			@RequestParam(value = "categoryId", required = false) Long categoryId
	) {
		Long userId = currentUserService.requireUserId(auth);
		java.time.YearMonth ym = java.time.YearMonth.parse(month);

		var entries = entryService.listByMonthWithFilters(userId, ym, type, paid, categoryId).stream()
				.map(e -> new EntryResponse(
						e.getId(), e.getEntryType(), e.getDescription(), e.getAmount(), e.getEntryDate(),
						e.isPaid(), e.getCategoryId(), e.getGroupId(), e.getInstallmentNo()
				))
				.toList();

		return ResponseEntity.ok(entries);
	}

	@PatchMapping("/{id}/paid")
	public ResponseEntity<EntryResponse> updatePaid(
			Authentication auth,
			@PathVariable("id") Long id,
			@Valid @RequestBody UpdatePaidRequest req
	) {
		Long userId = currentUserService.requireUserId(auth);

		var entry = entryService.updatePaid(userId, id, req.paid());

		return ResponseEntity.ok(new EntryResponse(
				entry.getId(), entry.getEntryType(), entry.getDescription(), entry.getAmount(), entry.getEntryDate(),
				entry.isPaid(), entry.getCategoryId(), entry.getGroupId(), entry.getInstallmentNo()
		));
	}

	@PostMapping
	public ResponseEntity<EntryResponse> createSingle(
			Authentication auth,
			@Valid @RequestBody CreateEntryRequest req
	) {
		Long userId = currentUserService.requireUserId(auth);

		var e = entryService.createSingleEntry(userId, req);

		return ResponseEntity.ok(new EntryResponse(
				e.getId(), e.getEntryType(), e.getDescription(), e.getAmount(), e.getEntryDate(),
				e.isPaid(), e.getCategoryId(), e.getGroupId(), e.getInstallmentNo()
		));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntryResponse> update(
			Authentication auth,
			@PathVariable("id") Long id,
			@Valid @RequestBody UpdateEntryRequest req
	) {
		Long userId = currentUserService.requireUserId(auth);

		var e = entryService.updateEntry(userId, id, req);

		return ResponseEntity.ok(new EntryResponse(
				e.getId(), e.getEntryType(), e.getDescription(), e.getAmount(), e.getEntryDate(),
				e.isPaid(), e.getCategoryId(), e.getGroupId(), e.getInstallmentNo()
		));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			Authentication auth,
			@PathVariable("id") Long id
	) {
		Long userId = currentUserService.requireUserId(auth);
		entryService.deleteEntry(userId, id);
		return ResponseEntity.noContent().build();
	}
}