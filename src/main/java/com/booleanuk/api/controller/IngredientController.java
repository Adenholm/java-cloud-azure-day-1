package com.booleanuk.api.controller;

import com.booleanuk.api.model.Ingredient;
import com.booleanuk.api.model.Recipe;
import com.booleanuk.api.repository.IngredientRepository;
import com.booleanuk.api.repository.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("ingredients")
public class IngredientController {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public IngredientController(IngredientRepository ingredientRepository,
                                RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll() {
        return ResponseEntity.ok(this.ingredientRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable("id") Integer id) {
        Ingredient ingredient = this.ingredientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find ingredient with that id."));
        return ResponseEntity.ok(ingredient);
    }

    // DTO for creation
    private record IngredientRequest(String ingredient, Integer recipeId) {}

    @PostMapping
    public ResponseEntity<Ingredient> create(@RequestBody IngredientRequest request) {
        Recipe recipe = recipeRepository.findById(request.recipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find recipe with that id."));

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredient(request.ingredient());
        ingredient.setRecipe(recipe);

        return new ResponseEntity<>(this.ingredientRepository.save(ingredient), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Ingredient> update(@PathVariable int id, @RequestBody IngredientRequest request) {
        Ingredient ingredientToUpdate = this.ingredientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find ingredient with that id."));

        Recipe recipe = recipeRepository.findById(request.recipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find recipe with that id."));

        ingredientToUpdate.setIngredient(request.ingredient());
        ingredientToUpdate.setRecipe(recipe);

        return ResponseEntity.ok(this.ingredientRepository.save(ingredientToUpdate));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Ingredient> delete(@PathVariable int id) {
        Ingredient ingredientToDelete = this.ingredientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find ingredient with that id."));
        this.ingredientRepository.delete(ingredientToDelete);
        return ResponseEntity.ok(ingredientToDelete);
    }
}
