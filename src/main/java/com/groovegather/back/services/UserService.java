package com.groovegather.back.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groovegather.back.dto.user.UserPostDto;
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

        // You can handle genre mapping and saving here

        return userDtoMapper.toUserDto(userEntity);
    }
}