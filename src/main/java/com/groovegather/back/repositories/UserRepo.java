package com.groovegather.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groovegather.back.entities.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    void deleteByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
