package com.cookpadidn.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient extends AbstractAuditableEntity{

    public Ingredient(String ingredient, Short measure, UOM unitOfMeasure) {
        this.ingredient = ingredient;
        this.measure = measure;
        this.unitOfMeasure = unitOfMeasure;
    }

    private String ingredient;
    private Short measure;
    private UOM unitOfMeasure;

    @ManyToOne
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    private Recipe recipe;

}
