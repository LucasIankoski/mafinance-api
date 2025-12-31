package com.app.mafinance.controllers;

import com.app.mafinance.dtos.CategoryResponse;
import com.app.mafinance.dtos.CreateCategoryRequest;
import com.app.mafinance.dtos.UpdateCategoryRequest;
import com.app.mafinance.model.enums.EntryType;
import com.app.mafinance.services.CategoryService;
import com.app.mafinance.services.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	private final CategoryService service;
	private final CurrentUserService currentUserService;

	public CategoryController(CategoryService service, CurrentUserService currentUserService) {
		this.service = service;
		this.currentUserService = currentUserService;
	}

	@PostMapping
	public ResponseEntity<CategoryResponse> create(
			Authentication auth,
			@Valid @RequestBody CreateCategoryRequest req
	) {
		Long userId = currentUserService.requireUserId(auth);
		var c = service.create(userId, req);
		return ResponseEntity.ok(new CategoryResponse(c.getId(), c.getType(), c.getName()));
	}

	@GetMapping
	public ResponseEntity<List<CategoryResponse>> list(
			Authentication auth,
			@RequestParam("type") EntryType type
	) {
		Long userId = currentUserService.requireUserId(auth);
		var out = service.list(userId, type).stream()
				.map(c -> new CategoryResponse(c.getId(), c.getType(), c.getName()))
				.toList();
		return ResponseEntity.ok(out);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponse> update(
			Authentication auth,
			@PathVariable("id") Long id,
			@Valid @RequestBody UpdateCategoryRequest req
	) {
		Long userId = currentUserService.requireUserId(auth);
		var c = service.update(userId, id, req);
		return ResponseEntity.ok(new CategoryResponse(c.getId(), c.getType(), c.getName()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			Authentication auth,
			@PathVariable("id") Long id
	) {
		Long userId = currentUserService.requireUserId(auth);
		service.delete(userId, id);
		return ResponseEntity.noContent().build();
	}
}