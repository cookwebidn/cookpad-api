package com.cookpadidn.entity;

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

    private String ingredient;
    private Short measure;
    private UOM unitOfMeasure;

    @ManyToOne
    private Recipe recipe;

}
