package com.groovegather.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.operate.OperateDto;
import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.enums.OperateRoleEnum;
import com.groovegather.back.repositories.OperateRepo;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.dtoMappers.OperateDtoMapper;

@Service
public class OperateService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OperateRepo operateRepo;

    @Autowired
    private OperateDtoMapper operateDtoMapper;

    public List<OperateDto> getUserProjects(UserDetails userDetails) {

        UserEntity user = getCurrentUser(userDetails.getUsername());

        List<OperateEntity> operationsEntities = operateRepo.findByUserAndRole(user, OperateRoleEnum.OWNER)
                .orElseThrow();

        List<OperateDto> operationsDto = operateDtoMapper.toOperateDtos(operationsEntities);
        return operationsDto;
    }

    private UserEntity getCurrentUser(String username) {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean canEditProject(UserDetails userDetails, String projectName) {
        UserEntity user = getCurrentUser(userDetails.getUsername());
        Optional<List<OperateEntity>> operations = operateRepo.findByUserAndRole(user, OperateRoleEnum.OWNER);
        return operations.map(ops -> ops.stream().anyMatch(op -> op.getProject().getName().equals(projectName)))
                .orElse(false);
    }

}
