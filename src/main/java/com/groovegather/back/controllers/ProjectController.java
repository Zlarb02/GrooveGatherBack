package com.groovegather.back.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groovegather.back.dtos.project.GetProject;
import com.groovegather.back.dtos.project.PostProject;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.services.ProjectService;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<Collection<GetProject>> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<PostProject> getByName(@PathVariable String name) {
        return ResponseEntity.ok(this.projectService.getByName(name));
    }

    @PostMapping
    public ResponseEntity<PostProject> createProject(@ModelAttribute PostProject projectPostDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        PostProject createdProject = projectService.createProject(projectPostDto, userDetails);
        return ResponseEntity.ok(createdProject);
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> update(@RequestBody ProjectEntity project, @PathVariable String name) {
        try {
            Optional<ProjectEntity> opProject = this.projectRepo.findByName(name);
            if (opProject.isPresent()) {
                project.setName(name);
                this.projectRepo.save(project);
                return ResponseEntity.ok(project);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> delete(@PathVariable String name) {
        this.projectRepo.deleteByName(name);
        return ResponseEntity.ok().build();
    }

    /*
     * @PutMapping("/{name}/likes")
     * public ResponseEntity<Void> incrementLikes2(@PathVariable String
     * name, @RequestParam int likesToAdd) {
     * projectService.incrementLikes2(name, likesToAdd);
     * return ResponseEntity.ok().build();
     * }
     */

}
