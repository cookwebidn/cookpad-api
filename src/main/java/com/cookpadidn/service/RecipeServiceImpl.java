package com.cookpadidn.service;

import com.cookpadidn.dto.IngredientRequest;
import com.cookpadidn.dto.RecipeRequest;
import com.cookpadidn.entity.Ingredient;
import com.cookpadidn.entity.Recipe;
import com.cookpadidn.entity.Step;
import com.cookpadidn.repository.IngredientRepository;
import com.cookpadidn.repository.RecipeRepository;
import com.cookpadidn.repository.StepRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        List<IngredientRequest> ingredients = recipeRequest.getIngredients();
        List<Ingredient> listOfConvertedIngredient = ingredients.stream().map(ingredientRequest -> Ingredient.builder().ingredient(ingredientRequest.getIngredient())
                .measure(ingredientRequest.getMeasure())
                .unitOfMeasure(ingredientRequest.getUnitOfMeasure()).build()).toList();
        List<Ingredient> savedIngredient = ingredientRepository.saveAllAndFlush(listOfConvertedIngredient);
        List<Step> listOfConvertedSteps = recipeRequest.getSteps().stream().map(stepRequest -> Step.builder()
                .steps(stepRequest.getSteps())
                .photoUrls(stepRequest.getPhotoUrls()).build()).toList();
        List<Step> savedSteps = stepRepository.saveAll(listOfConvertedSteps);
        Recipe recipe = Recipe.builder().photoUrl(recipeRequest.getPhotoUrl())
                .recipeTag(recipeRequest.getRecipeTag())
                .ingredients(savedIngredient)
                .steps(savedSteps).build();
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(String id) {
       return recipeRepository.findById(UUID.fromString(id))
               .orElseThrow(() -> {throw new RuntimeException("Can't find recipe with id : " + id);});
    }

    @Override
    public Recipe updateRecipe(RecipeRequest recipeRequest, String id) {
        Recipe recipe = recipeRepository.findById(UUID.fromString(id)).orElseThrow();
        List<IngredientRequest> ingredients = recipeRequest.getIngredients();
        List<Ingredient> listOfConvertedIngredient = ingredients.stream().map(ingredientRequest -> Ingredient.builder().ingredient(ingredientRequest.getIngredient())
                .measure(ingredientRequest.getMeasure())
                .unitOfMeasure(ingredientRequest.getUnitOfMeasure()).build()).toList();
        List<Ingredient> savedIngredient = ingredientRepository.saveAllAndFlush(listOfConvertedIngredient);
        List<Step> listOfConvertedSteps = recipeRequest.getSteps().stream().map(stepRequest -> Step.builder()
                .steps(stepRequest.getSteps())
                .photoUrls(stepRequest.getPhotoUrls()).build()).toList();
        List<Step> savedSteps = stepRepository.saveAll(listOfConvertedSteps);
        recipe.setPhotoUrl(recipeRequest.getPhotoUrl());
        recipe.setRecipeTag(recipeRequest.getRecipeTag());
        recipe.setIngredients(savedIngredient);
        recipe.setSteps(savedSteps);
        return recipeRepository.save(recipe);
    }
}
