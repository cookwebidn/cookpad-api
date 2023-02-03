package com.cookpadidn.repository;

import com.cookpadidn.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

}
