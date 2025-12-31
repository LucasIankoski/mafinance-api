package com.app.mafinance.services;

import com.app.mafinance.dtos.CreateInstallmentExpenseRequest;
import com.app.mafinance.dtos.CreateRecurringIncomeRequest;
import com.app.mafinance.model.Entry;
import com.app.mafinance.model.EntryGroup;
import com.app.mafinance.model.enums.EntryType;
import com.app.mafinance.model.enums.GroupType;
import com.app.mafinance.repositories.CategoryRepository;
import com.app.mafinance.repositories.EntryGroupRepository;
import com.app.mafinance.repositories.EntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.mafinance.infra.errors.NotFoundException;

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
	private final CategoryRepository categoryRepo;

	public EntryService(EntryRepository entryRepo, EntryGroupRepository groupRepo, CategoryRepository categoryRepo) {
		this.entryRepo = entryRepo;
		this.groupRepo = groupRepo;
		this.categoryRepo = categoryRepo;
	}

	@Transactional
	public List<Entry> createInstallmentExpense(Long userId, CreateInstallmentExpenseRequest req) {
		BigDecimal total = req.totalAmount().setScale(2, RoundingMode.HALF_UP);
		Long categoryId = validateCategory(userId, req.categoryId(), EntryType.EXPENSE);

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
			e.setCategoryId(categoryId);
			entries.add(e);
		}

		return entryRepo.saveAll(entries);
	}

	@Transactional
	public List<Entry> createRecurringIncome(Long userId, CreateRecurringIncomeRequest req) {
		Long categoryId = validateCategory(userId, req.categoryId(), EntryType.INCOME);
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
			e.setCategoryId(categoryId);
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
		BigDecimal remainder = total.subtract(sumBase);

		int pennies = remainder.movePointRight(2).intValueExact();

		List<BigDecimal> out = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			BigDecimal add = (i < pennies) ? new BigDecimal("0.01") : BigDecimal.ZERO;
			out.add(base.add(add));
		}
		return out;
	}

	@Transactional
	public Entry updatePaid(Long userId, Long entryId, boolean paid) {
		var entry = entryRepo.findByIdAndUserId(entryId, userId)
				.orElseThrow(() -> new NotFoundException("Entry not found."));

		entry.setPaid(paid);
		return entryRepo.save(entry);
	}

	private Long validateCategory(Long userId, Long categoryId, EntryType expectedType) {
		if (categoryId == null) {
			return null;
		}

		var cat = categoryRepo.findByIdAndUserId(categoryId, userId)
				.orElseThrow(() -> new NotFoundException("Category not found."));

		if (cat.getType() != expectedType) {
			throw new IllegalArgumentException("Category type does not match entry type.");
		}

		return cat.getId();
	}

	@Transactional
	public Entry createSingleEntry(Long userId, com.app.mafinance.dtos.CreateEntryRequest req) {
		var type = req.type();

		Long categoryId = validateCategory(userId, req.categoryId(), type);

		var e = new com.app.mafinance.model.Entry();
		e.setUserId(userId);
		e.setGroupId(null);
		e.setCategoryId(categoryId);
		e.setEntryType(type);
		e.setDescription(req.description().trim());
		e.setAmount(req.amount().setScale(2, java.math.RoundingMode.HALF_UP));
		e.setEntryDate(req.date());
		e.setPaid(req.paid() != null && req.paid());

		return entryRepo.save(e);
	}

	@Transactional
	public com.app.mafinance.model.Entry updateEntry(Long userId, Long entryId, com.app.mafinance.dtos.UpdateEntryRequest req) {
		var entry = entryRepo.findByIdAndUserId(entryId, userId)
				.orElseThrow(() -> new NotFoundException("Entry not found."));

		Long categoryId = validateCategory(userId, req.categoryId(), entry.getEntryType());

		entry.setDescription(req.description().trim());
		entry.setAmount(req.amount().setScale(2, java.math.RoundingMode.HALF_UP));
		entry.setEntryDate(req.date());
		entry.setCategoryId(categoryId);

		return entryRepo.save(entry);
	}

	@Transactional
	public void deleteEntry(Long userId, Long entryId) {
		var entry = entryRepo.findByIdAndUserId(entryId, userId)
				.orElseThrow(() -> new NotFoundException("Entry not found."));
		entryRepo.delete(entry);
	}

	@Transactional(readOnly = true)
	public java.util.List<com.app.mafinance.model.Entry> listByMonthWithFilters(
			Long userId,
			java.time.YearMonth month,
			com.app.mafinance.model.enums.EntryType type,
			Boolean paid,
			Long categoryId
	) {
		var start = month.atDay(1);
		var end = month.atEndOfMonth();
		return entryRepo.findMonthWithFilters(userId, start, end, type, paid, categoryId);
	}
}

