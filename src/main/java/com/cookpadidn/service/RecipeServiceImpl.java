package com.cookpadidn.service;

import com.cookpadidn.dto.RecipeRequest;
import com.cookpadidn.entity.Recipe;
import com.cookpadidn.repository.RecipeRepository;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe addRecipe(RecipeRequest recipeRequest) {
        Recipe recipe = Recipe.builder().photoUrl(recipeRequest.getPhotoUrl())
                .recipeTag(recipeRequest.getRecipeTag())
                .ingredients(recipeRequest.getIngredients())
                .steps(recipeRequest.getSteps()).build();
        return recipeRepository.save(recipe);
    }
}
