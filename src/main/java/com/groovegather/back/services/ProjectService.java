package com.groovegather.back.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groovegather.back.entities.GenreEntity;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.ProjectRepo;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private GenreRepo genreRepo;

    @Transactional
    public ProjectEntity createProject(ProjectEntity projectEntity) {
        List<GenreEntity> genres = new ArrayList<>();
        for (GenreEntity genre : projectEntity.getGenres()) {
            GenreEntity existingGenre = genreRepo.findById(genre.getName()).orElse(null);
            if (existingGenre == null) {
                existingGenre = genreRepo.save(new GenreEntity(genre.getName()));
            }
            genres.add(existingGenre);
        }
        projectEntity.setGenres(genres);
        return projectRepo.save(projectEntity);
    }
}
