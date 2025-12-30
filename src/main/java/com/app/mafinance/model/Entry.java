package com.app.mafinance.model;

import com.app.mafinance.model.enums.EntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "entry")
public class Entry {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="user_id", nullable=false)
	private Long userId;

	@Column(name="category_id")
	private Long categoryId;

	@Column(name="group_id")
	private Long groupId;

	@Enumerated(EnumType.STRING)
	@Column(name="entry_type", nullable=false, length=20)
	private EntryType entryType;

	@Column(nullable=false, length=180)
	private String description;

	@Column(nullable=false, precision=19, scale=2)
	private BigDecimal amount;

	@Column(name="entry_date", nullable=false)
	private LocalDate entryDate;

	@Column(nullable=false)
	private boolean paid = false;

	@Column(name="installment_no")
	private Integer installmentNo;

	@Column(name="created_at", nullable=false)
	private OffsetDateTime createdAt;

	@Column(name="updated_at", nullable=false)
	private OffsetDateTime updatedAt;

	@PrePersist
	void prePersist() {
		var now = OffsetDateTime.now();
		if (createdAt == null) createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	void preUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}

