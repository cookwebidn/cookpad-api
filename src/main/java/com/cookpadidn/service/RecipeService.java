package com.cookpadidn.service;

import com.cookpadidn.dto.RecipeRequest;
import com.cookpadidn.entity.Recipe;

public interface RecipeService {

    Recipe addRecipe(RecipeRequest recipeRequest);

}