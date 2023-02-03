package com.cookpadidn.controller;

import com.cookpadidn.dto.RecipeRequest;
import com.cookpadidn.entity.Recipe;
import com.cookpadidn.response.SuccessResponse;
import com.cookpadidn.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recipe/addrecipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> addRecipe(@RequestBody RecipeRequest recipeRequest){
        Recipe recipe = recipeService.addRecipe(recipeRequest);
        RecipeRequest recipeDTO = RecipeRequest.builder().photoUrl(recipe.getPhotoUrl())
                .recipeTag(recipe.getRecipeTag())
                .ingredients(recipe.getIngredients())
                .steps(recipe.getSteps())
                .build();
        SuccessResponse addRecipeSuccessfully = SuccessResponse.builder()
                .success(Boolean.TRUE)
                .message("add recipe successfully")
                .data(recipeDTO)
                .build();
        return new ResponseEntity<>(addRecipeSuccessfully, HttpStatus.CREATED);
    }
}
