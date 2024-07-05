package com.groovegather.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.SkillEntity;

public interface SkillRepo extends JpaRepository<SkillEntity, String> {

    Optional<SkillEntity> findByName(String name);

}
