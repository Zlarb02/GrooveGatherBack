package com.groovegather.back.repositories;

import org.springframework.data.repository.CrudRepository;

import com.groovegather.back.entities.SkillEntity;

public interface SkillRepo extends CrudRepository<SkillEntity, String> {

}
