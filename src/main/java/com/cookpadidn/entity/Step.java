package com.cookpadidn.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Step extends AbstractAuditableEntity {

    private String steps;
    private List<String> photoURls;
}
