package com.groovegather.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.OperateEntity;

public interface OperateRepo extends JpaRepository<OperateEntity, Long> {

}
