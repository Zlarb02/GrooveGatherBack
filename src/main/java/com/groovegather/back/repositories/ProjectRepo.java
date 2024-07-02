package com.groovegather.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.ProjectEntity;

public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {

}
