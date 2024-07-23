package com.groovegather.back.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.OperateId;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.enums.OperateRoleEnum;

public interface OperateRepo extends JpaRepository<OperateEntity, OperateId> {
   Optional<List<OperateEntity>> findByUserAndRole(UserEntity user, OperateRoleEnum role);

   Optional<List<OperateEntity>> findByProjectAndRole(ProjectEntity project, OperateRoleEnum role);

   Optional<List<OperateEntity>> findByUserAndRoleIn(UserEntity user, List<OperateRoleEnum> roles);

}
