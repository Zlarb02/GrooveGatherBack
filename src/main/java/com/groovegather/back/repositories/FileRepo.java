package com.groovegather.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.FileEntity;

public interface FileRepo extends JpaRepository<FileEntity, String> {

}
