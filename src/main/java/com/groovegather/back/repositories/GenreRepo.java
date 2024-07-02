package com.groovegather.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.GenreEntity;

public interface GenreRepo extends JpaRepository<GenreEntity, String> {

    GenreEntity findByName(String name);

}
