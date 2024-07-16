package com.groovegather.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.ProjectEntity;

public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByName(String name);

    void deleteByName(String name);

  /*   default void incrementLikes(Long projectId, int likesToAdd) {
        ProjectEntity project = findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("No project found with ID: " + projectId));
        project.setLikes(project.getLikes() + likesToAdd);
        save(project);
    } */

    default void incrementLikes2(String name, int likesToAdd) {
        ProjectEntity project = findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("No project found with name: " + name));
        project.setLikes(project.getLikes() + likesToAdd);
        save(project);
    }


}
