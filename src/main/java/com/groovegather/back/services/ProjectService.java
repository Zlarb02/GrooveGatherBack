package com.groovegather.back.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.project.GetProject;
import com.groovegather.back.dtos.project.PostProject;
import com.groovegather.back.entities.GenreEntity;
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

        List<GenreEntity> genres = new ArrayList<>();
        for (GenreEntity genre : projectEntity.getGenres()) {
            GenreEntity existingGenre = genreRepo.findById(genre.getName()).orElse(null);
            if (existingGenre == null) {
                existingGenre = genreRepo.save(new GenreEntity(genre.getName()));
            }
            genres.add(existingGenre);
        }
        projectEntity.setGenres(genres);

        projectEntity = projectRepo.save(projectEntity);
        return projectDtoMapper.toProjectPostDto(projectEntity);
    }

    public List<GetProject> getAll() {
        return projectDtoMapper.toGetProjectsDto(projectRepo.findAll());
    }
}
