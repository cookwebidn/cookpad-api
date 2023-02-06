package com.cookpadidn.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Step extends AbstractAuditableEntity {

    public Step(String steps, List<String> photoUrls) {
        this.steps = steps;
        this.photoUrls = photoUrls;
    }

    private String steps;
    private List<String> photoUrls = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    private Recipe recipe;
}
