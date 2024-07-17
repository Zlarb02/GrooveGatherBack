package com.groovegather.back.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.project.GetProject;
import com.groovegather.back.dtos.project.PostProject;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.services.dtoMappers.ProjectDtoMapper;

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

    public PostProject getByName(String name) {
        ProjectEntity projectEntity = projectRepo.findByName(name).get();
        return projectDtoMapper.toProjectPostDto(projectEntity);
    }



/*     public void incrementLikes2(String name, int likesToAdd) {
        projectRepo.incrementLikes2(name, likesToAdd);
    } */




   /*  public void incrementLikes2(String name, int likesToAdd, @AuthenticationPrincipal UserEntity user) {
        ProjectEntity project = projectRepo.findByName(name).orElseThrow(() -> new RuntimeException("Project not found"));
        
        Optional<OperateEntity> existingOperate = operateRepo.findByOperationAndUserAndProject("LIKE", user.getId(), project.getId());

        if (existingOperate.isPresent()) {
            OperateEntity operate = existingOperate.get();
            if (!"+1".equals(operate.getOperationContent())) {
                operate.setOperation(OperateEnum.LIKE);
                operate.setOperationContent("+1");
                operate.setRole(OperateRoleEnum.VIEWER);
                operate.setTimestamp(new Timestamp(System.currentTimeMillis()));
                operateRepo.save(operate);
            }
        } else {
            OperateEntity operate = new OperateEntity();
            OperateId operateId = new OperateId(user.getId(), project.getId());
            operate.setId(operateId);
            operate.setUser(user);
            operate.setProject(project);
            operate.setTimestamp(new Timestamp(System.currentTimeMillis()));
            operate.setOperation(OperateEnum.LIKE);
            operate.setOperationContent("+1");
            operate.setRole(OperateRoleEnum.VIEWER);
            operateRepo.save(operate);
        }

        project.setLikes(project.getLikes() + likesToAdd);
        projectRepo.save(project);
    }
 */

    
}
