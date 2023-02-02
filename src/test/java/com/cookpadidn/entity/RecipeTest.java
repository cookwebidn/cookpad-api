package com.cookpadidn.entity;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;


public class RecipeTest {

    @Test
    void testRecipe() {
        UUID uuid = UUID.randomUUID();
        String photoUrl = "https://example.com/recipe.jpg";
        RecipeTag recipeTag = RecipeTag.APPETIZERS;

        Recipe recipe = new Recipe("https://example.com/recipe.jpg", RecipeTag.APPETIZERS);
        recipe.setId(uuid);

        assertEquals(uuid, recipe.getId());
        assertEquals(photoUrl, recipe.getPhotoUrl());
        assertEquals(recipeTag, recipe.getRecipeTag());
    }

    @Test
    void recipeRelationTest() {
        UUID uuid = UUID.randomUUID();
        String photoUrl = "https://example.com/recipe.jpg";
        RecipeTag recipeTag = RecipeTag.APPETIZERS;
        Short measure = 2;
        Ingredient ingredient1 = new Ingredient("Sugar", measure, UOM.TEASPOON);
        Ingredient ingredient2 = new Ingredient("Sugar", measure, UOM.TEASPOON);

        List<Ingredient> ingredients = List.of(ingredient1, ingredient2);

        Recipe recipe = new Recipe("https://example.com/recipe.jpg", RecipeTag.APPETIZERS);
        recipe.setId(uuid);
        recipe.setIngredients(ingredients);

    }
}
