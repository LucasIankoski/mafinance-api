package com.app.mafinance.repositories;

import com.app.mafinance.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {

	List<Entry> findByUserIdAndEntryDateBetweenOrderByEntryDateAsc(Long userId, LocalDate start, LocalDate end);

	Optional<Entry> findByIdAndUserId(Long id, Long userId);
}

