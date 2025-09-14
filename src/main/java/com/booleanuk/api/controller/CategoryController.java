package com.booleanuk.api.controller;

import com.booleanuk.api.model.Category;
import com.booleanuk.api.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(this.categoryRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> getById(@PathVariable("id") Integer id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find category with that id."));
        return ResponseEntity.ok(category);
    }

    private record CategoryRequest(String name) {}

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());

        return new ResponseEntity<>(this.categoryRepository.save(category), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> update(@PathVariable int id, @RequestBody CategoryRequest request) {
        Category categoryToUpdate = this.categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find category with that id."));

        categoryToUpdate.setName(request.name());

        return ResponseEntity.ok(this.categoryRepository.save(categoryToUpdate));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Category> delete(@PathVariable int id) {
        Category categoryToDelete = this.categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find category with that id."));
        this.categoryRepository.delete(categoryToDelete);
        return ResponseEntity.ok(categoryToDelete);
    }
}
