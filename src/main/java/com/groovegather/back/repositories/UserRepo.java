package com.groovegather.back.repositories;

import org.springframework.data.repository.CrudRepository;

import com.groovegather.back.entities.UserEntity;

public interface UserRepo extends CrudRepository<UserEntity, Long> {

}
