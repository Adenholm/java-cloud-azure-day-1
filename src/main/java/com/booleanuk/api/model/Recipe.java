package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String imageUrl;

    @Column
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIncludeProperties({"ingredient"})
    private List<Ingredient> ingredients;

    @Column
    private String instructions;

    @Column
    private int prepTime;

    @Column
    private int cookingTime;

    @Column
    private int servings;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    @JsonIncludeProperties({"id", "name"})
    private Category category;

    public Recipe() {
    }

    public Recipe(String title, String description, String imageUrl, List<Ingredient> ingredients, String instructions, int prepTime, int cookingTime, int servings, Category category) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.cookingTime = cookingTime;
        this.servings = servings;
        this.category = category;
    }
}
