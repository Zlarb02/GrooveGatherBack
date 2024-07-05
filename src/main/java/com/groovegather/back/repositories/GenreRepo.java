package com.groovegather.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.GenreEntity;

public interface GenreRepo extends JpaRepository<GenreEntity, String> {

    Optional<GenreEntity> findByName(String name);

}
