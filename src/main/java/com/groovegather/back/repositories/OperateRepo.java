package com.groovegather.back.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.OperateId;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.enums.OperateRoleEnum;

public interface OperateRepo extends JpaRepository<OperateEntity, OperateId> {
   Optional<List<OperateEntity>> findByUserAndRole(UserEntity user, OperateRoleEnum role);
}
