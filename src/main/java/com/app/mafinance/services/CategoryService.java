package com.app.mafinance.services;

import com.app.mafinance.dtos.CreateCategoryRequest;
import com.app.mafinance.dtos.UpdateCategoryRequest;
import com.app.mafinance.model.Category;
import com.app.mafinance.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

	private final CategoryRepository repo;

	public CategoryService(CategoryRepository repo) {
		this.repo = repo;
	}

	@Transactional
	public Category create(Long userId, CreateCategoryRequest req) {
		String name = normalize(req.name());

		if (repo.existsByUserIdAndTypeAndNameIgnoreCase(userId, req.type(), name)) {
			throw new IllegalArgumentException("Category already exists for this type.");
		}

		var cat = new Category();
		cat.setUserId(userId);
		cat.setType(req.type());
		cat.setName(name);

		return repo.save(cat);
	}

	@Transactional(readOnly = true)
	public List<Category> list(Long userId, com.app.mafinance.model.enums.EntryType type) {
		return repo.findByUserIdAndTypeOrderByNameAsc(userId, type);
	}

	@Transactional
	public Category update(Long userId, Long categoryId, UpdateCategoryRequest req) {
		var cat = repo.findByIdAndUserId(categoryId, userId)
				.orElseThrow(() -> new IllegalArgumentException("Category not found."));

		String name = normalize(req.name());

		if (!cat.getName().equalsIgnoreCase(name)
				&& repo.existsByUserIdAndTypeAndNameIgnoreCase(userId, cat.getType(), name)) {
			throw new IllegalArgumentException("Category already exists for this type.");
		}

		cat.setName(name);
		return repo.save(cat);
	}

	@Transactional
	public void delete(Long userId, Long categoryId) {
		var cat = repo.findByIdAndUserId(categoryId, userId)
				.orElseThrow(() -> new IllegalArgumentException("Category not found."));
		repo.delete(cat);
	}

	private static String normalize(String s) {
		return s == null ? null : s.trim();
	}
}