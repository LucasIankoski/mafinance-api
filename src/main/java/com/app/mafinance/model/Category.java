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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
@Setter
@Table(name = "category",
		uniqueConstraints = @UniqueConstraint(name = "ux_category_user_name_type", columnNames = {"user_id","name","type"}))
public class Category {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="user_id", nullable=false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=20)
	private EntryType type;

	@Column(nullable=false, length=80)
	private String name;

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

