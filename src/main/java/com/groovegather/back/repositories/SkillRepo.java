package com.groovegather.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.SkillEntity;

public interface SkillRepo extends JpaRepository<SkillEntity, String> {

    SkillEntity findByName(String name);

}
