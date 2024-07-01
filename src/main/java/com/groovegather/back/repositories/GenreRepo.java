package com.groovegather.back.repositories;

import org.springframework.data.repository.CrudRepository;

import com.groovegather.back.entities.GenreEntity;

public interface GenreRepo extends CrudRepository<GenreEntity, String> {

}
