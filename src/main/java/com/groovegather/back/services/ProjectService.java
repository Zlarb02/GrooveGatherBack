package com.groovegather.back.services;

import java.util.List;

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
        projectEntity = projectRepo.save(projectEntity);
        return projectDtoMapper.toProjectPostDto(projectEntity);
    }

    public List<GetProject> getAll() {
        List<ProjectEntity> projectEntities = projectRepo.findAll();
        return projectDtoMapper.toGetProjectsDto(projectEntities);
    }
}
