package com.groovegather.back.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groovegather.back.dto.project.ProjectPostDto;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.services.dto.ProjectDtoMapper;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private ProjectDtoMapper projectDtoMapper; // Mapper class for ProjectEntity and ProjectPostDto

    @Transactional
    public ProjectEntity createProject(ProjectPostDto projectPostDto) {
        ProjectEntity projectEntity = projectDtoMapper.toProjectEntity(projectPostDto);

        // You can handle genre mapping and saving here

        return projectRepo.save(projectEntity);
    }
}
