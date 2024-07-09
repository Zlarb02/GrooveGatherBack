package com.groovegather.back.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.user.GetUser;
import com.groovegather.back.dtos.user.UserPostDto;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.errors.LoginException;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.dtos.UserDtoMapper;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserDtoMapper userDtoMapper; // Mapper class for UserEntity and UserPostDto

    public UserPostDto createUser(UserPostDto userPostDto) throws LoginException {
        this.validatePassword(userPostDto.getPassword());

        UserEntity userEntity = userDtoMapper.toUserEntity(userPostDto);

        Boolean userIsAlreadyExist = isUserAlreadyExistAndNotGoogle(userPostDto);
        Boolean userGoogleIsAlreadyExist = isUserAlreadyExistAndGoogle(userPostDto);

        if ((Boolean.TRUE.equals(userPostDto.getIsGoogle()) && !userGoogleIsAlreadyExist)
                || (!userIsAlreadyExist && userEntity.getPassword().equals(userEntity.getRepeatedPassword()))) {
            userRepo.save(userEntity);
            return userDtoMapper.toUserDto(userEntity);
        } else if (userIsAlreadyExist && Boolean.FALSE.equals(userPostDto.getIsGoogle())) {
            throw new LoginException("L'adresse email est déja utilisé pour un compte non google.");
        } else if (Boolean.FALSE.equals(userPostDto.getIsGoogle())) {
            throw new LoginException("Les mots de passes ne sont pas identiques.");
        } else if (userGoogleIsAlreadyExist && Boolean.TRUE.equals(userPostDto.getIsGoogle())) {
            throw new LoginException("L'adresse email est déja utilisé pour un compte google.");
        }
        return null;
    }

    public UserPostDto logUser(UserPostDto userPostDto, boolean isLogin) throws LoginException {

        // Utilisez Optional pour éviter les NoSuchElementException
        Optional<UserEntity> userOptional = userRepo.findByEmailAndIsGoogleFalse(userPostDto.getEmail());

        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            if (userPostDto.getPassword().equals(userEntity.getPassword())) {
                return userDtoMapper.toUserDto(userEntity);
            } else {
                throw new LoginException("Mot de passe incorrect.");
            }
        } else {
            throw new LoginException("Utilisateur non trouvé.");
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

    public boolean isUserAlreadyExistAndNotGoogle(UserPostDto userPostDto) {
        Optional<UserEntity> user = userRepo.findByEmailAndIsGoogleFalse(userPostDto.getEmail());
        return user.isPresent();
    }

    public boolean isUserAlreadyExistAndGoogle(UserPostDto userPostDto) {
        Optional<UserEntity> user = userRepo.findByEmailAndIsGoogleTrue(userPostDto.getEmail());
        return user.isPresent();
    }

    private void validatePassword(String password) throws LoginException {
        if (password == null || password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*") || !password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new LoginException(
                    "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.");
        }
    }
}