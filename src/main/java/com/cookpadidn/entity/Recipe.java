package com.cookpadidn.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe extends AbstractAuditableEntity {
    private String photoUrl;
    private RecipeTag recipeTag;
}
