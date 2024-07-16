package com.groovegather.back.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.project.GetProject;
import com.groovegather.back.dtos.project.PostProject;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.services.dtos.ProjectDtoMapper;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private ProjectDtoMapper projectDtoMapper;

    public PostProject createProject(PostProject projectPostDto) {
        ProjectEntity projectEntity = projectDtoMapper.toProjectEntity(projectPostDto);
        projectRepo.save(projectEntity);
        return projectDtoMapper.toProjectPostDto(projectEntity);
    }

    public Collection<GetProject> getAll() {
        Collection<ProjectEntity> projectEntities = projectRepo.findAll();
        return projectDtoMapper.toGetProjectsDto(projectEntities);
    }
    public PostProject getById(Long id){
        ProjectEntity projectEntity = projectRepo.findById(id).get();
        return projectDtoMapper.toProjectPostDto(projectEntity);
    }

/*     public void incrementLikes(Long projectId, int likesToAdd) {
        projectRepo.incrementLikes(projectId, likesToAdd);
    } */

    public void incrementLikes2(String name, int likesToAdd) {
        projectRepo.incrementLikes2(name, likesToAdd);
    }

}
