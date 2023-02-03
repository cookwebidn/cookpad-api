package com.cookpadidn.dto;

import com.cookpadidn.entity.Ingredient;
import com.cookpadidn.entity.RecipeTag;
import com.cookpadidn.entity.Step;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"id", "hibernateLazyInitializer", "handler"}, allowGetters = true)
public class RecipeRequest {

    private String id;
    private String photoUrl;
    private RecipeTag recipeTag;
    private List<Ingredient> ingredients = new ArrayList<>();

    private List<Step> steps  = new ArrayList<>();
}
