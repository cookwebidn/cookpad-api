package com.cookpadidn.service;

import com.cookpadidn.repository.IngredientRepository;
import com.cookpadidn.repository.RecipeRepository;
import com.cookpadidn.repository.StepRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    StepRepository stepRepository;

    @Test
    void createRecipeSuccess() {

    }
}
