package com.cookpadidn.entity;


import org.junit.jupiter.api.Test;

import java.util.UUID;


public class RecipeTest {

    @Test
    void testRecipe() {
        UUID uuid = UUID.randomUUID();
        String photoUrl = "https://example.com/recipe.jpg";
        RecipeTag recipeTag = RecipeTag.CONDIMENT;
        Ingredients recipeIngredients = new Inggredients();
        Step recipeStep = new Step();

        Recipe recipe = new Recipe(uuid, photoUrl, recipeTag, recipeIngredients, recipeStep);

        assertEquals(uuid, recipe.getUuid());
        assertEquals(photoUrl, recipe.getPhotoUrl());
        assertEquals(recipeTag, recipe.getRecipeTag());

    }



}
