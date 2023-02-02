package com.cookpadidn.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class IngredientsTest {


    @Test
    void ingredientsTest() {
        UUID uuid = UUID.randomUUID();
        String ingredientName = "Mentega";
        UOM ounch = UOM.ounch;

        Ingredient ingredient = new Ingredient();
        assertEquals(uuid, ingredient.getUuid());
        assertEquals(ingredientName, ingredient.getIngredientName());
        assertEquals(ounch, ingredient.getUOM());
    }

    @Test
    void ingredientsTestFail() {
        UUID uuid = UUID.randomUUID();
        int ingredientName = 1;
        UOM ounch = UOM.gram;

        Ingredient ingredient = new Ingredient();

        assertNotEquals(uuid, ingredient.getUuid());
        assertNotEquals(ingredientName, ingredient.getIngredientName());
        assertNotEquals(ounch, ingredient.getUOM());
    }
}
