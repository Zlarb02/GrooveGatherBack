package com.groovegather.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.FileEntity;

public interface FileRepo extends JpaRepository<FileEntity, Long> {
    FileEntity findByFileHash(String fileHash);

    Optional<FileEntity> findByUrl(String url);
}
