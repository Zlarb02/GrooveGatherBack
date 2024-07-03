package com.groovegather.back.services.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groovegather.back.dto.project.PostProject;
import com.groovegather.back.entities.GenreEntity;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.entities.ProjectSkillEntity;
import com.groovegather.back.entities.SkillEntity;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.SkillRepo;

@Component
public class ProjectDtoMapper {

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private SkillRepo skillRepo;

    public ProjectEntity toProjectEntity(PostProject projectPostDto) {
        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setName(projectPostDto.getName());
        projectEntity.setDescription(projectPostDto.getDescription());
        projectEntity.setColor(projectPostDto.getColor());
        projectEntity.setLikes(projectPostDto.getLikes() != null ? projectPostDto.getLikes() : 0);

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
        List<ProjectSkillEntity> projectSkills = new ArrayList<>();
        projectSkills.addAll(mapSkills(projectPostDto.getSkillsPresent(), projectEntity, false));
        projectSkills.addAll(mapSkills(projectPostDto.getSkillsMissing(), projectEntity, true));
        projectEntity.setProjectSkills(projectSkills);

        return projectEntity; // Ne pas sauvegarder ici, retournez simplement l'entité
    }

    private List<ProjectSkillEntity> mapSkills(List<String> skillNames, ProjectEntity projectEntity,
            boolean isMissing) {
        List<ProjectSkillEntity> projectSkills = new ArrayList<>();
        if (skillNames != null) {
            for (String skillName : skillNames) {
                // Vérifier si la compétence existe déjà
                SkillEntity skillEntity = skillRepo.findByName(skillName);
                if (skillEntity == null) {
                    // Si la compétence n'existe pas, la créer
                    skillEntity = new SkillEntity();
                    skillEntity.setName(skillName);
                    skillEntity = skillRepo.save(skillEntity);
                }
                // Créer la relation entre le projet et la compétence
                ProjectSkillEntity projectSkill = new ProjectSkillEntity();
                projectSkill.setProject(projectEntity);
                projectSkill.setSkill(skillEntity);
                projectSkill.setIsMissing(isMissing);
                projectSkills.add(projectSkill);
            }
        }
        return projectSkills;
    }

    public PostProject toProjectPostDto(ProjectEntity projectEntity) {
        PostProject projectPostDto = new PostProject();
        projectPostDto.setName(projectEntity.getName());
        projectPostDto.setDescription(projectEntity.getDescription());
        projectPostDto.setColor(projectEntity.getColor());
        projectPostDto.setLikes(projectEntity.getLikes());

        projectPostDto.setGenres(projectEntity.getGenres().stream()
                .map(GenreEntity::getName)
                .collect(Collectors.toList()));

        projectPostDto.setSkillsPresent(projectEntity.getProjectSkills().stream()
                .filter(projectSkill -> !projectSkill.getIsMissing())
                .map(projectSkill -> projectSkill.getSkill().getName())
                .collect(Collectors.toList()));

        projectPostDto.setSkillsMissing(projectEntity.getProjectSkills().stream()
                .filter(ProjectSkillEntity::getIsMissing)
                .map(projectSkill -> projectSkill.getSkill().getName())
                .collect(Collectors.toList()));

        return projectPostDto;
    }

}
