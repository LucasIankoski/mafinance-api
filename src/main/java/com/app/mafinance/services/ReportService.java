package com.app.mafinance.services;

import com.app.mafinance.dtos.MonthlyCategoryTotalResponse;
import com.app.mafinance.dtos.MonthlyReportResponse;
import com.app.mafinance.repositories.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class ReportService {

	private final ReportRepository reportRepo;

	public ReportService(ReportRepository reportRepo) {
		this.reportRepo = reportRepo;
	}

	@Transactional(readOnly = true)
	public MonthlyReportResponse monthly(Long userId, YearMonth month, boolean includeByCategory) {
		LocalDate start = month.atDay(1);
		LocalDate end = month.atEndOfMonth();

		var totals = reportRepo.monthlyTotals(userId, start, end);

		BigDecimal income = nz(totals.getIncome());
		BigDecimal expense = nz(totals.getExpense());
		BigDecimal balance = income.subtract(expense);

		List<MonthlyCategoryTotalResponse> byCategory = List.of();
		if (includeByCategory) {
			byCategory = reportRepo.totalsByCategory(userId, start, end).stream()
					.map(p -> new MonthlyCategoryTotalResponse(
							p.getType(),
							p.getCategoryId(),
							p.getCategoryName(),
							nz(p.getTotal())
					))
					.toList();
		}

		return new MonthlyReportResponse(
				month.toString(),
				income,
				expense,
				balance,
				byCategory
		);
	}

	private static BigDecimal nz(BigDecimal v) {
		return v == null ? BigDecimal.ZERO : v;
	}
}