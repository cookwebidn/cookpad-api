package com.cookpadidn.entity;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class StepTest {

    @Test
    void StepTest() {
        UUID uuid = UUID.randomUUID();
        String step = "Put that into boiling water";
        List<String> stepPhotoUrl = List.of("http://somestepimage.com","http://somestepimage2.com");

        Step steps = new Step(step, stepPhotoUrl);

        assertEquals(uuid, steps.getUuid());
        assertEquals(ingredientName, steps.getIngredientName());
        assertEquals(ounch, steps.getUOM());

    }
}
