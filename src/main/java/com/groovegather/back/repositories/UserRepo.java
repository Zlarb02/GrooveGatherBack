package com.groovegather.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groovegather.back.entities.UserEntity;

import jakarta.transaction.Transactional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.isGoogle = false")
    Optional<UserEntity> findByEmailAndIsGoogleFalse(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.isGoogle = true")
    Optional<UserEntity> findByEmailAndIsGoogleTrue(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserEntity u WHERE u.email = :email AND u.isGoogle = false")
    void deleteByEmailAndIsGoogleFalse(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserEntity u WHERE u.email = :email AND u.isGoogle = true")
    void deleteByEmailAndIsGoogleTrue(@Param("email") String email);
}
