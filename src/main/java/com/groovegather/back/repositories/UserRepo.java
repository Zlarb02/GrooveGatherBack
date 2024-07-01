package com.groovegather.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

}
