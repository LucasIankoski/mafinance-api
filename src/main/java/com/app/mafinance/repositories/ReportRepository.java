package com.app.mafinance.repositories;

import com.app.mafinance.repositories.CategoryTotalsProjection;
import com.app.mafinance.repositories.MonthlyTotalsProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends Repository<com.app.mafinance.model.Entry, Long> {

	@Query(value = """
			SELECT
			  COALESCE(SUM(CASE WHEN e.entry_type = 'INCOME' THEN e.amount ELSE 0 END), 0) AS income,
			  COALESCE(SUM(CASE WHEN e.entry_type = 'EXPENSE' THEN e.amount ELSE 0 END), 0) AS expense
			FROM entry e
			WHERE e.user_id = :userId
			  AND e.entry_date BETWEEN :startDate AND :endDate
			""", nativeQuery = true)
	MonthlyTotalsProjection monthlyTotals(
			@Param("userId") Long userId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate
	);

	@Query(value = """
			SELECT
			  e.entry_type AS type,
			  e.category_id AS categoryId,
			  COALESCE(c.name, 'Sem categoria') AS categoryName,
			  COALESCE(SUM(e.amount), 0) AS total
			FROM entry e
			LEFT JOIN category c ON c.id = e.category_id AND c.user_id = e.user_id
			WHERE e.user_id = :userId
			  AND e.entry_date BETWEEN :startDate AND :endDate
			GROUP BY e.entry_type, e.category_id, c.name
			ORDER BY e.entry_type, total DESC
			""", nativeQuery = true)
	List<CategoryTotalsProjection> totalsByCategory(
			@Param("userId") Long userId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate
	);
}