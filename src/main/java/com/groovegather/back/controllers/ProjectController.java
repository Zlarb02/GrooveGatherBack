package com.groovegather.back.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.services.ProjectService;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectRepo projectRepo;

    public ProjectController(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    @GetMapping("")
    public Iterable<ProjectEntity> getAll() {
        return this.projectRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ProjectEntity> getById(@PathVariable Long id) {
        return this.projectRepo.findById(id);
    }

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectEntity> createProject(@RequestBody ProjectEntity projectEntity) {
        ProjectEntity createdProject = projectService.createProject(projectEntity);
        return ResponseEntity.ok(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ProjectEntity project, @PathVariable Long id) {
        try {
            Optional<ProjectEntity> opProject = this.projectRepo.findById(id);
            if (opProject.isPresent()) {
                project.setId(id);
                this.projectRepo.save(project);
                return ResponseEntity.ok(project);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.projectRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}