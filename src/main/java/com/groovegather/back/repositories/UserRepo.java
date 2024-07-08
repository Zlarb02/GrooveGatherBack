package com.groovegather.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.groovegather.back.entities.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.isGoogle = true")
    Optional<UserEntity> findByEmailAndIsGoogleTrue(@Param("email") String email);

}
