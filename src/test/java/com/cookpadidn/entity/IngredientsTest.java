package com.cookpadidn.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngredientsTest {


    @Test
    void ingredientsTest() {
        UUID uuid = UUID.randomUUID();
        String ingredientName = "Mentega";
        UOM ounch = UOM.OUNCH;
        short measure = 2;
        Ingredient ingredient = new Ingredient("Mentega", measure, UOM.OUNCH);
        ingredient.setId(uuid);

        assertEquals(uuid, ingredient.getId());
        assertEquals(ingredientName, ingredient.getIngredient());
        assertEquals(ounch, ingredient.getUnitOfMeasure());
    }

    @Test
    void ingredientsTestFail() {
        UUID uuid = UUID.randomUUID();
        int ingredientName = 1;
        UOM ounch = UOM.GRAM;

        Ingredient ingredient = new Ingredient();

        Assertions.assertNotEquals(uuid, ingredient.getId());
        Assertions.assertNotEquals(ingredientName, ingredient.getIngredient());
        Assertions.assertNotEquals(ounch, ingredient.getUnitOfMeasure());
    }
}
