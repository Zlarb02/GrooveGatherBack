package com.groovegather.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.ProjectEntity;

public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByName(String name);
    Optional<ProjectEntity> findById(Long Id);

}
