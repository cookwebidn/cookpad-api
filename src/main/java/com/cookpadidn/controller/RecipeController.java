package com.cookpadidn.controller;

import com.cookpadidn.dto.IngredientRequest;
import com.cookpadidn.dto.RecipeRequest;
import com.cookpadidn.dto.StepRequest;
import com.cookpadidn.entity.Recipe;
import com.cookpadidn.response.SuccessResponse;
import com.cookpadidn.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/addrecipe")
    public ResponseEntity<SuccessResponse> addRecipe(@RequestBody RecipeRequest recipeRequest){
        Recipe recipe = recipeService.addRecipe(recipeRequest);
        RecipeRequest recipeDTO = RecipeRequest.builder().photoUrl(recipe.getPhotoUrl())
                .id(recipe.getId().toString())
                .recipeTag(recipe.getRecipeTag())
                .ingredients(recipe.getIngredients().stream().map(ingredient -> IngredientRequest.builder()
                        .ingredient(ingredient.getIngredient())
                        .measure(ingredient.getMeasure())
                        .unitOfMeasure(ingredient.getUnitOfMeasure()).build()).toList())
                .steps(recipe.getSteps().stream().map(step -> StepRequest.builder()
                        .steps(step.getSteps())
                        .photoUrls(step.getPhotoUrls()).build()).toList())
                .build();
        SuccessResponse addRecipeSuccessfully = SuccessResponse.builder()
                .success(Boolean.TRUE)
                .message("add recipe successfully")
                .data(recipeDTO)
                .build();
        return new ResponseEntity<>(addRecipeSuccessfully, HttpStatus.CREATED);
    }

    @GetMapping("/getrecipe")
    public ResponseEntity<SuccessResponse> getRecipe(@RequestParam("id") String id){
        Recipe recipe = recipeService.getRecipeById(id);
        RecipeRequest recipeDTO = RecipeRequest.builder().photoUrl(recipe.getPhotoUrl())
                .id(recipe.getId().toString())
                .recipeTag(recipe.getRecipeTag())
                .ingredients(recipe.getIngredients().stream().map(ingredient -> IngredientRequest.builder()
                        .ingredient(ingredient.getIngredient())
                        .measure(ingredient.getMeasure())
                        .unitOfMeasure(ingredient.getUnitOfMeasure()).build()).toList())
                .steps(recipe.getSteps().stream().map(step -> StepRequest.builder()
                        .steps(step.getSteps())
                        .photoUrls(step.getPhotoUrls()).build()).toList())
                .build();
        SuccessResponse getRecipeSuccessfully = SuccessResponse.builder()
                .success(Boolean.TRUE)
                .message("get recipe successfully")
                .data(recipeDTO)
                .build();
        return new ResponseEntity<>(getRecipeSuccessfully, HttpStatus.CREATED);
    }

    @PutMapping("/updaterecipe")
    public ResponseEntity<SuccessResponse> updatedRecipe(@RequestParam("id") String id,
                                                         @RequestBody RecipeRequest recipeRequest) {
        Recipe recipe = recipeService.updateRecipe(recipeRequest, id);
        RecipeRequest recipeDTO = RecipeRequest.builder().photoUrl(recipe.getPhotoUrl())
                .id(recipe.getId().toString())
                .recipeTag(recipe.getRecipeTag())
                .ingredients(recipe.getIngredients().stream().map(ingredient -> IngredientRequest.builder()
                        .ingredient(ingredient.getIngredient())
                        .measure(ingredient.getMeasure())
                        .unitOfMeasure(ingredient.getUnitOfMeasure()).build()).toList())
                .steps(recipe.getSteps().stream().map(step -> StepRequest.builder()
                        .steps(step.getSteps())
                        .photoUrls(step.getPhotoUrls()).build()).toList())
                .build();
        SuccessResponse getRecipeSuccessfully = SuccessResponse.builder()
                .success(Boolean.TRUE)
                .message("updated recipe successfully")
                .data(recipeDTO)
                .build();
        return new ResponseEntity<>(getRecipeSuccessfully, HttpStatus.CREATED);
    }

    @DeleteMapping("/deletedrecipe")
    public ResponseEntity<SuccessResponse> deletedRecipe(@RequestParam("id") String id) {
        recipeService.deleteRecipe(id);
        SuccessResponse getRecipeSuccessfully = SuccessResponse.builder()
                .success(Boolean.TRUE)
                .message("deleted recipe successfully")
                .build();
        return new ResponseEntity<>(getRecipeSuccessfully, HttpStatus.CREATED);
    }

}


