package com.cookpadidn.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient extends AbstractAuditableEntity{

    public Ingredient(String ingredient, Short measure, UOM unitOfMeasure) {
        this.ingredient = ingredient;
        this.measure = measure;
        this.unitOfMeasure = unitOfMeasure;
    }

    private String ingredient;
    private Short measure;
    private UOM unitOfMeasure;

    @ManyToOne(cascade = CascadeType.ALL)
    private Recipe recipe;

}
