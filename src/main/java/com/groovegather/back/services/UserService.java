package com.groovegather.back.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.user.GetUser;
import com.groovegather.back.dtos.user.UserPostDto;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.errors.PasswordMismatchException;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.dtos.UserDtoMapper;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserDtoMapper userDtoMapper; // Mapper class for UserEntity and UserPostDto

    public UserPostDto createUser(UserPostDto userPostDto) throws PasswordMismatchException {
        UserEntity userEntity = userDtoMapper.toUserEntity(userPostDto);

        if (Boolean.TRUE.equals(userPostDto.getIsGoogle())
                || userEntity.getPassword().equals(userEntity.getRepeatedPassword())) {
            userRepo.save(userEntity);
            return userDtoMapper.toUserDto(userEntity);
        } else {
            throw new PasswordMismatchException("Les mots de passes ne sont pas identiques.");
        }
    }

    public Collection<GetUser> getAll() {

        Collection<UserEntity> users = userRepo.findAll();

        return userDtoMapper.toUsersDtos(users);
    }

    public UserPostDto getGoogleUserByEmail(String email) {

        Optional<UserEntity> user = userRepo.findByEmailAndIsGoogleTrue(email);
        return userDtoMapper.toUserDto(user.get());
    }
}