package com.groovegather.back.services.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groovegather.back.dto.project.ProjectPostDto;
import com.groovegather.back.dto.project.SkillDto;
import com.groovegather.back.entities.GenreEntity;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.entities.SkillEntity;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.repositories.SkillRepo;

@Component
public class ProjectDtoMapper {

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private SkillRepo skillRepo;

    public ProjectEntity toProjectEntity(ProjectPostDto projectPostDto) {
        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setName(projectPostDto.getName());
        projectEntity.setDescription(projectPostDto.getDescription());
        projectEntity.setColor(projectPostDto.getColor());

        // Mappage des genres
        List<GenreEntity> genres = new ArrayList<>();
        for (String genreName : projectPostDto.getGenres()) {
            GenreEntity genreEntity = genreRepo.findByName(genreName);
            if (genreEntity == null) {
                genreEntity = new GenreEntity(genreName);
                genreEntity = genreRepo.save(genreEntity);
            }
            genres.add(genreEntity);
        }
        projectEntity.setGenres(genres);

        // Mappage des skills
        List<SkillEntity> skills = new ArrayList<>();
        skills.addAll(mapSkills(projectPostDto.getSkillsPresent(), false));
        skills.addAll(mapSkills(projectPostDto.getSkillsMissing(), true));
        projectEntity.setSkills(skills);

        return projectRepo.save(projectEntity); // Sauvegarde de l'entité ProjectEntity après configuration
    }

    private List<SkillEntity> mapSkills(List<SkillDto> skillDtos, boolean isMissing) {
        List<SkillEntity> skills = new ArrayList<>();
        if (skillDtos != null) {
            for (SkillDto skillDto : skillDtos) {
                // Vérifier si la compétence existe déjà
                SkillEntity skillEntity = skillRepo.findByName(skillDto.getName());
                if (skillEntity == null) {
                    // Si la compétence n'existe pas, la créer avec isMissing défini
                    skillEntity = new SkillEntity();
                    skillEntity.setName(skillDto.getName());
                    skillEntity.setIsMissing(isMissing); // Définir l'état manquant/présent
                    skillEntity = skillRepo.save(skillEntity);
                } else {
                    // Si la compétence existe, mettre à jour isMissing si nécessaire
                    if (isMissing != skillEntity.getIsMissing()) {
                        skillEntity.setIsMissing(isMissing);
                        skillEntity = skillRepo.save(skillEntity);
                    }
                }
                skills.add(skillEntity);
            }
        }
        return skills;
    }
}
