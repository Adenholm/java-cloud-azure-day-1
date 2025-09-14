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
@Table(name="ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="ingredient")
    private String ingredient;

    @ManyToOne
    @JoinColumn(name="recipe_id", nullable=false)
    @JsonIncludeProperties({"id", "title"})
    private Recipe recipe;


    public Ingredient() {
    }

    public Ingredient(int id, String ingredient) {
        this.id = id;
        this.ingredient = ingredient;
    }
}
