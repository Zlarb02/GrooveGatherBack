package com.groovegather.back.repositories;

import org.springframework.data.repository.CrudRepository;

import com.groovegather.back.entities.FileEntity;

public interface FileRepo extends CrudRepository<FileEntity, Long> {

}
