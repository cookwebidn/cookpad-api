package com.cookpadidn.repository;

import com.cookpadidn.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StepRepository extends JpaRepository<Step, UUID> {

}
