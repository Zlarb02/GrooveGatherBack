package com.groovegather.back.repositories;

import org.springframework.data.repository.CrudRepository;

import com.groovegather.back.entities.ProjectEntity;

public interface ProjectRepo extends CrudRepository<ProjectEntity, Long> {

}
