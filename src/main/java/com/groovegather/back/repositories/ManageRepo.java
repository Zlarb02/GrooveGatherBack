package com.groovegather.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.ManageEntity;
import com.groovegather.back.entities.ManageId;

public interface ManageRepo extends JpaRepository<ManageEntity, ManageId> {

}
