package com.booleanuk.api.controller;

import com.booleanuk.api.model.Category;
import com.booleanuk.api.model.Ingredient;
import com.booleanuk.api.model.Recipe;
import com.booleanuk.api.repository.CategoryRepository;
import com.booleanuk.api.repository.IngredientRepository;
import com.booleanuk.api.repository.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("recipes")
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeController(RecipeRepository recipeRepository,
                            CategoryRepository categoryRepository,
                            IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAll() {
        return ResponseEntity.ok(this.recipeRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Recipe> getById(@PathVariable("id") Integer id) {
        Recipe recipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find recipe with that id."));
        return ResponseEntity.ok(recipe);
    }

    // DTO for creating/updating recipes
    private record RecipeRequest(
            String title,
            String description,
            String imageUrl,
            String instructions,
            int prepTime,
            int cookingTime,
            int servings,
            Integer categoryId,
            List<String> ingredients
    ) {}

    @PostMapping
    public ResponseEntity<Recipe> create(@RequestBody RecipeRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find category with that id."));

        Recipe recipe = new Recipe();
        recipe.setTitle(request.title());
        recipe.setDescription(request.description());
        recipe.setImageUrl(request.imageUrl());
        recipe.setInstructions(request.instructions());
        recipe.setPrepTime(request.prepTime());
        recipe.setCookingTime(request.cookingTime());
        recipe.setServings(request.servings());
        recipe.setCategory(category);

        Recipe savedRecipe = this.recipeRepository.save(recipe);

        List<Ingredient> ingredients = request.ingredients().stream()
                .map(name -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredient(name);
                    ingredient.setRecipe(savedRecipe);
                    return ingredientRepository.save(ingredient);
                })
                .toList();

        savedRecipe.setIngredients(ingredients);

        return new ResponseEntity<>(this.recipeRepository.save(savedRecipe), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Recipe> update(@PathVariable int id, @RequestBody RecipeRequest request) {
        Recipe recipeToUpdate = this.recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find recipe with that id."));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find category with that id."));

        recipeToUpdate.setTitle(request.title());
        recipeToUpdate.setDescription(request.description());
        recipeToUpdate.setImageUrl(request.imageUrl());
        recipeToUpdate.setInstructions(request.instructions());
        recipeToUpdate.setPrepTime(request.prepTime());
        recipeToUpdate.setCookingTime(request.cookingTime());
        recipeToUpdate.setServings(request.servings());
        recipeToUpdate.setCategory(category);

        // clear old ingredients
        ingredientRepository.deleteAll(recipeToUpdate.getIngredients());

        // add new ingredients
        List<Ingredient> ingredients = request.ingredients().stream()
                .map(name -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredient(name);
                    ingredient.setRecipe(recipeToUpdate);
                    return ingredientRepository.save(ingredient);
                })
                .toList();

        recipeToUpdate.setIngredients(ingredients);

        return ResponseEntity.ok(this.recipeRepository.save(recipeToUpdate));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Recipe> delete(@PathVariable int id) {
        Recipe recipeToDelete = this.recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find recipe with that id."));
        this.recipeRepository.delete(recipeToDelete);
        return ResponseEntity.ok(recipeToDelete);
    }
}


