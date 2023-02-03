package com.cookpadidn.service;

import com.cookpadidn.dto.RecipeRequest;
import com.cookpadidn.entity.*;
import com.cookpadidn.repository.IngredientRepository;
import com.cookpadidn.repository.RecipeRepository;
import com.cookpadidn.repository.StepRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    StepRepository stepRepository;

    @InjectMocks
    RecipeServiceImpl recipeService;


    @Test
    void createRecipeSuccess() {
        UUID uuid = UUID.randomUUID();
        String photoUrl = "https://example.com/recipe.jpg";
        RecipeTag recipeTag = RecipeTag.APPETIZERS;
        Short measure = 2;
        Ingredient ingredient1 = new Ingredient("Sugar", measure, UOM.TEASPOON);
        Ingredient ingredient2 = new Ingredient("Sugar", measure, UOM.TEASPOON);
        List<Ingredient> ingredients = List.of(ingredient1, ingredient2);

        //step
        String photoStepUrlOne = "https://example.com/recipe.jpg";
        String photoStepUrlTwo = "https://example.com/recipe.jpg";
        String photoStepUrlThree = "https://example.com/recipe.jpg";

        List<String> photoStepUrls = List.of(photoStepUrlOne, photoStepUrlTwo, photoStepUrlThree);
        Step putThatIntoBoilingWater = new Step("put that into boiling water", photoStepUrls);
        Step putThatIntoBoilingSoup = new Step("put that into boiling soup", photoStepUrls);

        List<Step> listOfStep = List.of(putThatIntoBoilingWater, putThatIntoBoilingSoup);


        Recipe recipe = new Recipe(photoUrl, recipeTag, ingredients, listOfStep);

        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        RecipeRequest recipeRequest = RecipeRequest.builder().recipeTag(recipe.getRecipeTag())
                .photoUrl(recipe.getPhotoUrl())
                .ingredients(recipe.getIngredients())
                .steps(recipe.getSteps()).build();

        Recipe savedRecipe =  recipeService.addRecipe(recipeRequest);

        System.out.println(recipeTag);
        System.out.println(savedRecipe.getRecipeTag());

        assertEquals(recipeTag, savedRecipe.getRecipeTag());

        verify(recipeRepository, times(1)).save(any(Recipe.class));

    }
}
