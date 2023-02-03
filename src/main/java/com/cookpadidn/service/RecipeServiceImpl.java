package com.cookpadidn.service;

import com.cookpadidn.dto.RecipeRequest;
import com.cookpadidn.entity.Ingredient;
import com.cookpadidn.entity.Recipe;
import com.cookpadidn.entity.Step;
import com.cookpadidn.repository.IngredientRepository;
import com.cookpadidn.repository.RecipeRepository;
import com.cookpadidn.repository.StepRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientRepository ingredientRepository;
    private final StepRepository stepRepository;
    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, StepRepository stepRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.stepRepository = stepRepository;
    }

    @Override
    public Recipe addRecipe(RecipeRequest recipeRequest) {
        List<Ingredient> ingredients = recipeRequest.getIngredients();
        List<Ingredient> savedIngredient = ingredientRepository.saveAllAndFlush(ingredients);
        List<Step> savedSteps = stepRepository.saveAll(recipeRequest.getSteps());
        Recipe recipe = Recipe.builder().photoUrl(recipeRequest.getPhotoUrl())
                .recipeTag(recipeRequest.getRecipeTag())
                .ingredients(savedIngredient)
                .steps(savedSteps).build();
//        savedIngredient.forEach(ingredient -> ingredient.setRecipe(recipe));
        return recipeRepository.save(recipe);
    }
}
