package com.groovegather.back.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.user.GetUser;
import com.groovegather.back.dtos.user.UserPostDto;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.dtos.UserDtoMapper;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserDtoMapper userDtoMapper; // Mapper class for UserEntity and UserPostDto

    public UserPostDto createUser(UserPostDto userPostDto) {
        UserEntity userEntity = userDtoMapper.toUserEntity(userPostDto);

        if (userEntity.getPassword() == userEntity.getRepeatedPassword()) {
            userRepo.save(userEntity);
            return userDtoMapper.toUserDto(userEntity);
        } else {
            return null;
        }
    }

    public Collection<GetUser> getAll() {

        Collection<UserEntity> users = userRepo.findAll();

        return userDtoMapper.toUsersDtos(users);
    }
}