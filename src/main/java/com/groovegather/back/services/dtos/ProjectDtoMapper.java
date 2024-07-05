package com.groovegather.back.services.dtos;

import java.util.ArrayList;
import java.util.List;
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
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.repositories.SkillRepo;

@Component
public class ProjectDtoMapper {

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private SkillRepo skillRepo;

    @Autowired
    private ProjectRepo projectRepo;

    public ProjectEntity toProjectEntity(PostProject projectPostDto) {
        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setName(projectPostDto.getName());
        projectEntity.setDescription(projectPostDto.getDescription());
        projectEntity.setColor(projectPostDto.getColor());
        projectEntity.setLikes(projectPostDto.getLikes() != null ? projectPostDto.getLikes() : 0);

        // Mappage des genres
        List<GenreEntity> genres = new ArrayList<>();
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
        List<ProjectSkillEntity> projectSkills = new ArrayList<>();
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

        projectRepo.save(projectEntity);

        return projectEntity;

    }
    // Mappage des skills
    // List<ProjectSkillEntity> projectSkills = new ArrayList<>();
    // projectSkills.addAll(mapSkills(projectPostDto.getSkillsPresent(),
    // projectEntity, false));
    // projectSkills.addAll(mapSkills(projectPostDto.getSkillsMissing(),
    // projectEntity, true));
    // projectEntity.setProjectSkills(projectSkills);

    // private List<SkillEntity> mapSkills(List<String> skillNames, ProjectEntity
    // projectEntity,
    // boolean isMissing) {
    // List<SkillEntity> skills = new ArrayList<>();
    // if (skillNames != null) {
    // for (String skillName : skillNames) {
    // SkillEntity skillEntity = skillRepo.findByName(skillName).orElse(null);
    // if (skillEntity == null) {
    // skillEntity = new SkillEntity(skillName);
    // skillEntity.setName(skillName);
    // skillRepo.save(skillEntity);
    // }

    // skills.add(skillEntity);
    // ProjectSkillEntity projectSkill = new ProjectSkillEntity();
    // projectSkill.setProject(projectEntity);
    // projectSkill.setSkill(skillEntity);
    // projectSkill.setIsMissing(isMissing);
    // projectSkills.add(projectSkill);
    // }
    // }
    // return skills;
    // }

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

    public List<GetProject> toGetProjectsDto(List<ProjectEntity> projectEntities) {
        List<GetProject> getProjectsDto = new ArrayList<>();

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
