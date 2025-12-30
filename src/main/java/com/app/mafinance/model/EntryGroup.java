package com.app.mafinance.model;

import com.app.mafinance.model.enums.GroupType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
@Table(name = "entry_group")
public class EntryGroup {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="user_id", nullable=false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name="group_type", nullable=false, length=30)
	private GroupType groupType;

	@Column(nullable=false, length=140)
	private String title;

	@Column(name="total_amount", precision=19, scale=2)
	private BigDecimal totalAmount;

	@Column(name="installments_count")
	private Integer installmentsCount;

	@Column(name="start_date")
	private LocalDate startDate;

	@Column(name="months_generated")
	private Integer monthsGenerated;

	@Column(name="created_at", nullable=false)
	private OffsetDateTime createdAt;

	@PrePersist
	void prePersist() {
		if (createdAt == null) createdAt = OffsetDateTime.now();
	}
}

