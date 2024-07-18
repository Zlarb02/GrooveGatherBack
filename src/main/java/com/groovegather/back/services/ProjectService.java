package com.groovegather.back.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.project.GetProject;
import com.groovegather.back.dtos.project.PostProject;
import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.enums.OperateEnum;
import com.groovegather.back.enums.OperateRoleEnum;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.OperateRepo;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.dtoMappers.ProjectDtoMapper;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private OperateRepo operateRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProjectDtoMapper projectDtoMapper;

    // Méthode pour récupérer l'utilisateur actuel
    private UserEntity getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Méthode pour créer un projet
    public PostProject createProject(PostProject projectPostDto, UserEntity user) {
        ProjectEntity projectEntity = projectDtoMapper.toProjectEntity(projectPostDto);

        operateRepo.save(new OperateEntity(OperateEnum.CREATE, projectEntity, user, OperateRoleEnum.OWNER));

        return projectDtoMapper.toProjectPostDto(projectEntity);
    }

    public Collection<GetProject> getAll() {
        Collection<ProjectEntity> projectEntities = projectRepo.findAll();
        return projectDtoMapper.toGetProjectsDto(projectEntities);
    }

    public PostProject getByName(String name) {
        ProjectEntity projectEntity = projectRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return projectDtoMapper.toProjectPostDto(projectEntity);
    }
}


/*   public void incrementLikes2(String name, int likesToAdd) {
 projectRepo.incrementLikes2(name, likesToAdd);
 }
 */

/*
 * public void incrementLikes2(String name, int
 * likesToAdd, @AuthenticationPrincipal UserEntity user) {
 * ProjectEntity project = projectRepo.findByName(name).orElseThrow(() -> new
 * RuntimeException("Project not found"));
 * 
 * Optional<OperateEntity> existingOperate =
 * operateRepo.findByOperationAndUserAndProject("LIKE", user.getId(),
 * project.getId());
 * 
 * if (existingOperate.isPresent()) {
 * OperateEntity operate = existingOperate.get();
 * if (!"+1".equals(operate.getOperationContent())) {
 * operate.setOperation(OperateEnum.LIKE);
 * operate.setOperationContent("+1");
 * operate.setRole(OperateRoleEnum.VIEWER);
 * operate.setTimestamp(new Timestamp(System.currentTimeMillis()));
 * operateRepo.save(operate);
 * }
 * } else {
 * OperateEntity operate = new OperateEntity();
 * OperateId operateId = new OperateId(user.getId(), project.getId());
 * operate.setId(operateId);
 * operate.setUser(user);
 * operate.setProject(project);
 * operate.setTimestamp(new Timestamp(System.currentTimeMillis()));
 * operate.setOperation(OperateEnum.LIKE);
 * operate.setOperationContent("+1");
 * operate.setRole(OperateRoleEnum.VIEWER);
 * operateRepo.save(operate);
 * }
 * 
 * project.setLikes(project.getLikes() + likesToAdd);
 * projectRepo.save(project);
 * }
 */
