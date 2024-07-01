package com.groovegather.back.repositories;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.ManageEntity;

public interface ManageRepo extends JpaRepository<ManageEntity, Timestamp> {

}
