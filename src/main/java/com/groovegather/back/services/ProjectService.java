package com.groovegather.back.services;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.project.FileDto;
import com.groovegather.back.dtos.project.GetProject;
import com.groovegather.back.dtos.project.PostProject;
import com.groovegather.back.entities.FileEntity;
import com.groovegather.back.entities.ManageEntity;
import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.enums.OperateEnum;
import com.groovegather.back.enums.OperateRoleEnum;
import com.groovegather.back.repositories.FileRepo;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.ManageRepo;
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

    @Autowired
    private ManageRepo manageRepo;

    @Autowired
    private FileRepo fileRepo;

    private UserEntity getCurrentUser(String username) {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public PostProject createProject(PostProject projectPostDto, UserDetails userDetails)
            throws SQLException {
        ProjectEntity projectEntity = projectDtoMapper.toProjectEntity(projectPostDto);
        UserEntity user = getCurrentUser(userDetails.getUsername());

        projectEntity = projectRepo.save(projectEntity);

        operateRepo.save(new OperateEntity(OperateEnum.CREATE, projectEntity, user, OperateRoleEnum.OWNER));

        Collection<ManageEntity> manageEntities = new ArrayList<>();
        for (FileDto fileDto : projectPostDto.getFiles()) {
            FileEntity fileEntity = fileRepo.findByUrl(fileDto.getUrl()).orElse(null);
            if (fileEntity == null) {
                FileEntity fileEntityNull = new FileEntity();
                fileEntityNull.setUrl(fileDto.getUrl());
                String fileName = fileDto.getUrl().substring(fileDto.getUrl().lastIndexOf("/") + 1);
                fileEntityNull.setName(fileName);
                fileEntityNull.setDescription("Description par défaut");
                fileEntityNull.setIsPrivate(false);
                fileEntityNull.setIsScore(false);
                fileEntityNull.setIsTeaser(fileDto.getIsTeaser());
                fileEntityNull
                        .setFileExtension('.' + fileDto.getUrl().substring(fileDto.getUrl().lastIndexOf(".") + 1));
                String fileHash = HashUtil.generateSHA256Hash(fileDto.getUrl());
                if (fileHash == null || fileHash.isEmpty()) {
                    throw new RuntimeException("File hash cannot be null or empty");
                }
                fileEntityNull.setFileHash(fileHash);
                fileEntityNull.setSize(new File(fileDto.getUrl()).length());
                fileEntity = fileRepo.save(fileEntityNull);
            }

            fileEntity.setIsTeaser(fileDto.getIsTeaser());
            ManageEntity manageEntity = new ManageEntity(projectEntity, fileEntity);
            manageEntities.add(manageEntity);
        }

        manageRepo.saveAll(manageEntities);

        projectEntity.setProjectManageFiles(manageEntities);

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

/*
 * public void incrementLikes2(String name, int likesToAdd) {
 * projectRepo.incrementLikes2(name, likesToAdd);
 * }
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
 * operate.setTimestamp(new LocalDate(System.currentTimeMillis()));
 * operateRepo.save(operate);
 * }
 * } else {
 * OperateEntity operate = new OperateEntity();
 * OperateId operateId = new OperateId(user.getId(), project.getId());
 * operate.setId(operateId);
 * operate.setUser(user);
 * operate.setProject(project);
 * operate.setTimestamp(new LocalDate(System.currentTimeMillis()));
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
