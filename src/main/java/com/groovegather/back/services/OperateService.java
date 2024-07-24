package com.groovegather.back.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.operate.OperateDto;
import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.enums.OperateRoleEnum;
import com.groovegather.back.repositories.OperateRepo;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.dtoMappers.OperateDtoMapper;

@Service
public class OperateService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OperateRepo operateRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private OperateDtoMapper operateDtoMapper;

    public List<OperateDto> getUserProjects(UserDetails userDetails) {
        UserEntity user = getCurrentUser(userDetails.getUsername());
        List<OperateEntity> operationsEntities = operateRepo.findByUserAndRole(user, OperateRoleEnum.OWNER)
                .orElseThrow();
        return operateDtoMapper.toOperateDtos(operationsEntities);
    }

    private UserEntity getCurrentUser(String username) {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean canEditProject(UserDetails userDetails, String projectName) {
        UserEntity user = getCurrentUser(userDetails.getUsername());
        List<OperateRoleEnum> roles = List.of(OperateRoleEnum.OWNER, OperateRoleEnum.ADMIN);
        Optional<List<OperateEntity>> operations = operateRepo.findByUserAndRoleIn(user, roles);
        return operations.map(ops -> ops.stream()
                .anyMatch(op -> op.getProject().getName().equals(projectName)))
                .orElse(false);
    }

    public UserEntity getProjectOwner(String projectName) {
        return operateRepo.findByProjectAndRole(getProjectEntity(projectName), OperateRoleEnum.OWNER)
                .orElseThrow(() -> new RuntimeException("Owner not found"))
                .get(0).getUser();
    }

    public List<UserEntity> getAdminsOfProject(UserDetails userDetails, String projectName) {
        UserEntity currentUser = getCurrentUser(userDetails.getUsername());
        if (!canEditProject(userDetails, projectName)) {
            throw new RuntimeException("Access Denied");
        }
        return operateRepo.findByProjectAndRole(getProjectEntity(projectName), OperateRoleEnum.ADMIN)
                .orElseThrow()
                .stream()
                .map(OperateEntity::getUser)
                .collect(Collectors.toList());
    }

    public List<OperateDto> getProjectsWhereUserIsAdmin(UserDetails userDetails) {
        UserEntity user = getCurrentUser(userDetails.getUsername());
        List<OperateEntity> operationsEntities = operateRepo.findByUserAndRole(user, OperateRoleEnum.ADMIN)
                .orElseThrow();
        return operateDtoMapper.toOperateDtos(operationsEntities);
    }

    private ProjectEntity getProjectEntity(String projectName) {
        // Assume you have a method to find a project by name
        return projectRepo.findByName(projectName)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
