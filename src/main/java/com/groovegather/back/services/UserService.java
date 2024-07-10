package com.groovegather.back.services;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.user.GetUserDto;
import com.groovegather.back.dtos.user.UserDto;
import com.groovegather.back.entities.GenreEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.errors.LoginException;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.dtos.UserDtoMapper;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private UserDtoMapper userDtoMapper; // Mapper class for UserEntity and UserPostDto

    public UserDto createUser(UserDto userPostDto) throws LoginException {
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

    public UserDto logUser(UserDto userPostDto, boolean isLogin) throws LoginException {

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

    public Collection<GetUserDto> getAll() {

        Collection<UserEntity> users = userRepo.findAll();

        return userDtoMapper.toUsersDtos(users);
    }

    public UserDto getById(Long id) {
        Optional<UserEntity> user = userRepo.findById(id);
        return userDtoMapper.toUserDto(user.get());
    }

    @Transactional
    public UserDto patchUser(UserDto userDto, UserDto userPatchDto) {
        // Charger l'entité existante depuis la base de données
        UserEntity userEntity = userRepo.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Appliquer les modifications de userPatchDto à l'entité existante
        updateEntityFromDto(userEntity, userPatchDto);

        // Sauvegarder l'entité mise à jour
        userRepo.save(userEntity);

        // Retourner le DTO mis à jour
        return userDtoMapper.toUserDto(userEntity);
    }

    private void updateEntityFromDto(UserEntity userEntity, UserDto userPatchDto) {
        Optional.ofNullable(userPatchDto.getName()).ifPresent(userEntity::setName);
        Optional.ofNullable(userPatchDto.getPassword()).ifPresent(password -> {
            userEntity.setPassword(password);
            userEntity.setRepeatedPassword(userPatchDto.getRepeatedPassword());
        });
        Optional.ofNullable(userPatchDto.getEmail()).ifPresent(userEntity::setEmail);
        Optional.ofNullable(userPatchDto.getPicture()).ifPresent(userEntity::setPicture);
        Optional.ofNullable(userPatchDto.getToken()).ifPresent(userEntity::setToken);
        Optional.ofNullable(userPatchDto.getDescription()).ifPresent(userEntity::setDescription);
        Optional.ofNullable(userPatchDto.getRole()).ifPresent(userEntity::setRole);
        Optional.ofNullable(userPatchDto.getSubscriptionLevel()).ifPresent(userEntity::setSubscriptionLevel);
        Optional.ofNullable(userPatchDto.getIsGoogle()).ifPresent(userEntity::setIsGoogle);

        if (userPatchDto.getGenres() != null) {
            Collection<GenreEntity> genres = userPatchDto.getGenres().stream()
                    .map(genreName -> genreRepo.findByName(genreName)
                            .orElseGet(() -> genreRepo.save(new GenreEntity(genreName))))
                    .collect(Collectors.toList());
            userEntity.setGenres(genres);
        }
    }

    public UserDto getGoogleUserByEmail(String email) {

        Optional<UserEntity> user = userRepo.findByEmailAndIsGoogleTrue(email);
        return userDtoMapper.toUserDto(user.get());
    }

    public boolean isUserAlreadyExistAndNotGoogle(UserDto userPostDto) {
        Optional<UserEntity> user = userRepo.findByEmailAndIsGoogleFalse(userPostDto.getEmail());
        return user.isPresent();
    }

    public boolean isUserAlreadyExistAndGoogle(UserDto userPostDto) {
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