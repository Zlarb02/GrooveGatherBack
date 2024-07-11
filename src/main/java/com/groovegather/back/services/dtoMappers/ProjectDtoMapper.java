package com.groovegather.back.services.dtoMappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groovegather.back.dtos.project.GetProject;
import com.groovegather.back.dtos.project.PostProject;
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
        Collection<GenreEntity> genres = new ArrayList<>();
        for (String genreName : projectPostDto.getGenres()) {
            GenreEntity genreEntity = genreRepo.findByName(genreName).orElse(null);
            if (genreEntity == null) {
                genreEntity = new GenreEntity(genreName);
                genreRepo.save(genreEntity);
            }
            genres.add(genreEntity);
        }
        projectEntity.setGenres(genres);

        // Mappage des Skills
        Collection<ProjectSkillEntity> projectSkills = new ArrayList<>();
        for (String skillPresentName : projectPostDto.getSkillsPresent()) {
            SkillEntity skillEntity = skillRepo.findByName(skillPresentName).orElse(null);
            if (skillEntity == null) {
                skillEntity = new SkillEntity(skillPresentName);

            }
            ProjectSkillEntity projectSkillEntity = new ProjectSkillEntity(projectEntity, skillEntity, false);

            projectSkills.add(projectSkillEntity);
        }
        for (String skillMissingName : projectPostDto.getSkillsMissing()) {
            SkillEntity skillEntity = skillRepo.findByName(skillMissingName).orElse(null);
            if (skillEntity == null) {
                skillEntity = new SkillEntity(skillMissingName);
            }
            ProjectSkillEntity projectSkillEntity = new ProjectSkillEntity(projectEntity, skillEntity, true);

            projectSkills.add(projectSkillEntity);
        }
        projectEntity.setProjectSkills(projectSkills);

        return projectEntity;

    }

    public PostProject toProjectPostDto(ProjectEntity projectEntity) {
        PostProject projectPostDto = new PostProject();
        projectPostDto.setName(projectEntity.getName());
        projectPostDto.setDescription(projectEntity.getDescription());
        projectPostDto.setColor(projectEntity.getColor());
        projectPostDto.setLikes(projectEntity.getLikes());
        projectPostDto.setDate(projectEntity.getDate());

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

    public Collection<GetProject> toGetProjectsDto(Collection<ProjectEntity> projectEntities) {
        Collection<GetProject> getProjectsDto = new ArrayList<>();

        for (ProjectEntity projectEntity : projectEntities) {
            GetProject projectGetDto = new GetProject();
            projectGetDto.setId(projectEntity.getId());
            projectGetDto.setName(projectEntity.getName());
            projectGetDto.setDescription(projectEntity.getDescription());
            projectGetDto.setColor(projectEntity.getColor());
            projectGetDto.setLikes(projectEntity.getLikes());
            projectGetDto.setDate(projectEntity.getDate());

            projectGetDto.setGenres(projectEntity.getGenres().stream()
                    .map(GenreEntity::getName)
                    .collect(Collectors.toList()));

            projectGetDto.setSkillsPresent(projectEntity.getProjectSkills().stream()
                    .filter(projectSkill -> !projectSkill.getIsMissing())
                    .map(projectSkill -> projectSkill.getSkill().getName())
                    .collect(Collectors.toList()));

            projectGetDto.setSkillsMissing(
                    projectEntity.getProjectSkills().stream()
                            .filter(ProjectSkillEntity::getIsMissing)
                            .map(projectSkill -> projectSkill.getSkill().getName())
                            .collect(Collectors.toList()));

            getProjectsDto.add(projectGetDto);
        }

        return getProjectsDto;
    }
}
