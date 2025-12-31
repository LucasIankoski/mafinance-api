package com.app.mafinance.repositories;

import com.app.mafinance.model.Entry;
import com.app.mafinance.model.enums.EntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {

	List<Entry> findByUserIdAndEntryDateBetweenOrderByEntryDateAsc(Long userId, LocalDate start, LocalDate end);

	Optional<Entry> findByIdAndUserId(Long id, Long userId);

	boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);

	@Query("""
			    SELECT e
			    FROM Entry e
			    WHERE e.userId = :userId
			      AND e.entryDate BETWEEN :start AND :end
			      AND (:type IS NULL OR e.entryType = :type)
			      AND (:paid IS NULL OR e.paid = :paid)
			      AND (:categoryId IS NULL OR e.categoryId = :categoryId)
			    ORDER BY e.entryDate ASC, e.id ASC
			""")
	List<Entry> findMonthWithFilters(
			@Param("userId") Long userId,
			@Param("start") java.time.LocalDate start,
			@Param("end") java.time.LocalDate end,
			@Param("type") EntryType type,
			@Param("paid") Boolean paid,
			@Param("categoryId") Long categoryId
	);
}

