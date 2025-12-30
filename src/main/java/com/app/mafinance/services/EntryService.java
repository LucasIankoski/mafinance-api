package com.app.mafinance.services;

import com.app.mafinance.dtos.CreateInstallmentExpenseRequest;
import com.app.mafinance.dtos.CreateRecurringIncomeRequest;
import com.app.mafinance.model.Entry;
import com.app.mafinance.model.EntryGroup;
import com.app.mafinance.model.enums.EntryType;
import com.app.mafinance.model.enums.GroupType;
import com.app.mafinance.repositories.EntryGroupRepository;
import com.app.mafinance.repositories.EntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntryService {

	private final EntryRepository entryRepo;
	private final EntryGroupRepository groupRepo;

	public EntryService(EntryRepository entryRepo, EntryGroupRepository groupRepo) {
		this.entryRepo = entryRepo;
		this.groupRepo = groupRepo;
	}

	@Transactional
	public List<Entry> createInstallmentExpense(Long userId, CreateInstallmentExpenseRequest req) {
		BigDecimal total = req.totalAmount().setScale(2, RoundingMode.HALF_UP);
		int n = req.installments();

		var group = new EntryGroup();
		group.setUserId(userId);
		group.setGroupType(GroupType.INSTALLMENT);
		group.setTitle(req.title().trim());
		group.setTotalAmount(total);
		group.setInstallmentsCount(n);
		group.setStartDate(req.firstDate());
		group = groupRepo.save(group);

		List<BigDecimal> amounts = splitIntoInstallments(total, n);

		List<Entry> entries = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			Entry e = new Entry();
			e.setUserId(userId);
			e.setGroupId(group.getId());
			e.setCategoryId(req.categoryId());
			e.setEntryType(EntryType.EXPENSE);
			e.setDescription(req.description().trim());
			e.setAmount(amounts.get(i));
			e.setEntryDate(req.firstDate().plusMonths(i));
			e.setInstallmentNo(i + 1);
			entries.add(e);
		}

		return entryRepo.saveAll(entries);
	}

	@Transactional
	public List<Entry> createRecurringIncome(Long userId, CreateRecurringIncomeRequest req) {
		int months = req.monthsToGenerate();
		BigDecimal amount = req.amount().setScale(2, RoundingMode.HALF_UP);

		var group = new EntryGroup();
		group.setUserId(userId);
		group.setGroupType(GroupType.RECURRING_INCOME);
		group.setTitle(req.title().trim());
		group.setTotalAmount(amount);
		group.setStartDate(req.startDate());
		group.setMonthsGenerated(months);
		group = groupRepo.save(group);

		List<Entry> entries = new ArrayList<>(months);
		for (int i = 0; i < months; i++) {
			Entry e = new Entry();
			e.setUserId(userId);
			e.setGroupId(group.getId());
			e.setCategoryId(req.categoryId());
			e.setEntryType(EntryType.INCOME);
			e.setDescription(req.description().trim());
			e.setAmount(amount);
			e.setEntryDate(req.startDate().plusMonths(i));
			entries.add(e);
		}

		return entryRepo.saveAll(entries);
	}

	@Transactional(readOnly = true)
	public List<Entry> listByMonth(Long userId, YearMonth month) {
		LocalDate start = month.atDay(1);
		LocalDate end = month.atEndOfMonth();
		return entryRepo.findByUserIdAndEntryDateBetweenOrderByEntryDateAsc(userId, start, end);
	}

	static List<BigDecimal> splitIntoInstallments(BigDecimal total, int n) {
		BigDecimal base = total.divide(BigDecimal.valueOf(n), 2, RoundingMode.DOWN);
		BigDecimal sumBase = base.multiply(BigDecimal.valueOf(n));
		BigDecimal remainder = total.subtract(sumBase); // 0.00 .. 0.(n-1) * 0.01

		int pennies = remainder.movePointRight(2).intValueExact();

		List<BigDecimal> out = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			BigDecimal add = (i < pennies) ? new BigDecimal("0.01") : BigDecimal.ZERO;
			out.add(base.add(add));
		}
		return out;
	}
}

