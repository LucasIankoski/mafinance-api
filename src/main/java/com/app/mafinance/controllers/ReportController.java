package com.app.mafinance.controllers;

import com.app.mafinance.dtos.MonthlyReportResponse;
import com.app.mafinance.services.ReportService;
import com.app.mafinance.services.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

	private final ReportService reportService;
	private final CurrentUserService currentUserService;

	public ReportController(ReportService reportService, CurrentUserService currentUserService) {
		this.reportService = reportService;
		this.currentUserService = currentUserService;
	}

	@GetMapping("/monthly")
	public ResponseEntity<MonthlyReportResponse> monthly(
			Authentication auth,
			@RequestParam("month") String month,
			@RequestParam(value = "byCategory", defaultValue = "true") boolean byCategory
	) {
		Long userId = currentUserService.requireUserId(auth);
		YearMonth ym = YearMonth.parse(month);
		return ResponseEntity.ok(reportService.monthly(userId, ym, byCategory));
	}
}