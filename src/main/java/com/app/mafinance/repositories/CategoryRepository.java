package com.app.mafinance.repositories;

import com.app.mafinance.model.Category;
import com.app.mafinance.model.enums.EntryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByUserIdAndTypeOrderByNameAsc(Long userId, EntryType type);

	Optional<Category> findByIdAndUserId(Long id, Long userId);

	boolean existsByUserIdAndTypeAndNameIgnoreCase(Long userId, EntryType type, String name);
}