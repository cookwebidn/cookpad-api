package com.cookpadidn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe extends AbstractAuditableEntity {

    public Recipe(String photoUrl, RecipeTag recipeTag) {
        this.photoUrl = photoUrl;
        this.recipeTag = recipeTag;
    }

    private String photoUrl;
    private RecipeTag recipeTag;
    @OneToMany(mappedBy = "recipe")
    private List<Ingredient> ingredients = new ArrayList<>();
    @OneToMany(mappedBy = "steps")
    private List<Step> steps  = new ArrayList<>();
}
