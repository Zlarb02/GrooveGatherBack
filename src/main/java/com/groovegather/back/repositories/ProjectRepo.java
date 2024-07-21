package com.groovegather.back.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groovegather.back.entities.ProjectEntity;

public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {

    @Query("SELECT p FROM ProjectEntity p JOIN p.genres g WHERE g.name IN :genres")
    Collection<ProjectEntity> findByGenres(@Param("genres") Collection<String> genres);

    @Query("SELECT p FROM ProjectEntity p JOIN p.projectSkills ps WHERE ps.skill.name IN :skills")
    Collection<ProjectEntity> findBySkills(@Param("skills") Collection<String> skills);

    Optional<ProjectEntity> findByName(String name);

    void deleteByName(String name);

    /*
     * default void incrementLikes(Long projectId, int likesToAdd) {
     * ProjectEntity project = findById(projectId)
     * .orElseThrow(() -> new IllegalArgumentException("No project found with ID: "
     * + projectId));
     * project.setLikes(project.getLikes() + likesToAdd);
     * save(project);
     * }
     */

    default void incrementLikes2(String name, int likesToAdd) {
        ProjectEntity project = findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("No project found with name: " + name));
        project.setLikes(project.getLikes() + likesToAdd);
        save(project);
    }

}
