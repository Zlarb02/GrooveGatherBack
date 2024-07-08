package com.groovegather.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.OperateId;

public interface OperateRepo extends JpaRepository<OperateEntity, OperateId> {

}
