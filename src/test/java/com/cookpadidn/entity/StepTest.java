package com.cookpadidn.entity;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepTest {

    @Test
    void StepTest() {
        UUID uuid = UUID.randomUUID();
        String step = "Put that into boiling water";
        List<String> stepPhotoUrl = List.of("http://somestepimage.com","http://somestepimage2.com");

        Step steps = new Step(step, stepPhotoUrl);
        steps.setId(uuid);

        assertEquals(uuid, steps.getId());
        assertEquals(step, steps.getSteps());
        assertEquals(stepPhotoUrl, steps.getPhotoURls());

    }
}
