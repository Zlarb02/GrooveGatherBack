package com.groovegather.back.services.dtos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groovegather.back.dtos.user.GetUserDto;
import com.groovegather.back.dtos.user.UserDto;
import com.groovegather.back.entities.GenreEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.repositories.GenreRepo;

@Component
public class UserDtoMapper {

    @Autowired
    private GenreRepo genreRepo;

    public UserEntity toUserEntity(UserDto userPostDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setIsGoogle(userPostDto.getIsGoogle());
        if (userPostDto.getName() != null && !userPostDto.getName().equals("")) {
            userEntity.setName(userPostDto.getName());
        } else {
            userEntity.setName(userPostDto.getEmail());
        }
        userEntity.setPassword(userPostDto.getPassword());
        userEntity.setRepeatedPassword(userPostDto.getRepeatedPassword());
        userEntity.setEmail(userPostDto.getEmail());
        if (userPostDto.getPicture() != null) {
            userEntity.setPicture(userPostDto.getPicture());
        } else {
            userEntity.setPicture("https://www.picsum.photos/150/150");
        }
        userEntity.setToken(userPostDto.getToken());
        if (userPostDto.getDescription() != null) {
            userEntity.setDescription(userPostDto.getDescription());
        } else {
            userEntity.setDescription("No description");
        }
        if (userPostDto.getRole() != null) {
            userEntity.setRole(userPostDto.getRole());
        } else {
            userEntity.setRole(0);
        }
        if (userPostDto.getSubscriptionLevel() != null) {
            userEntity.setSubscriptionLevel(userPostDto.getSubscriptionLevel());
        } else {
            userEntity.setSubscriptionLevel(0);
        }

        // Example of mapping genres (assuming genre names are sent as strings)
        if ((userPostDto.getGenres() != null))

        {
            Collection<GenreEntity> genres = new ArrayList<>();
            for (String genreName : userPostDto.getGenres()) {
                GenreEntity genreEntity = genreRepo.findByName(genreName).orElse(null);
                if (genreEntity == null) {
                    genreEntity = new GenreEntity(genreName);
                    genreEntity = genreRepo.save(genreEntity);
                }

                genres.add(genreEntity);
            }
            userEntity.setGenres(genres);
        }

        userEntity.setToken(" ");
        return userEntity;

    }

    public UserDto toUserDto(UserEntity userEntity) {
        UserDto userGetDto = new UserDto();
        userGetDto.setId(userEntity.getId());
        userGetDto.setName(userEntity.getName());
        userGetDto.setPassword("ok");
        userGetDto.setRepeatedPassword("ok");
        userGetDto.setEmail(userEntity.getEmail());
        userGetDto.setPicture(userEntity.getPicture());
        userGetDto.setToken(userEntity.getToken());
        userGetDto.setDescription(userEntity.getDescription());
        userGetDto.setRole(userEntity.getRole());
        userGetDto.setSubscriptionLevel(userEntity.getSubscriptionLevel());
        userGetDto.setGenres(userEntity.getGenres().stream()
                .map(GenreEntity::getName)
                .collect(Collectors.toList()));

        return userGetDto;
    }

    public Collection<GetUserDto> toUsersDtos(Collection<UserEntity> userEntities) {
        Collection<GetUserDto> userGetDtos = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            GetUserDto userGetDto = new GetUserDto();
            userGetDto.setId(userEntity.getId());
            userGetDto.setName(userEntity.getName());
            userGetDto.setPassword(userEntity.getPassword());
            userGetDto.setEmail(userEntity.getEmail());
            userGetDto.setPicture(userEntity.getPicture());
            userGetDto.setToken(userEntity.getToken());
            userGetDto.setDescription(userEntity.getDescription());
            userGetDto.setRole(userEntity.getRole());
            userGetDto.setSubscriptionLevel(userEntity.getSubscriptionLevel());
            userGetDto.setIsGoogle(userEntity.getIsGoogle());
            userGetDto.setGenres(userEntity.getGenres().stream()
                    .map(GenreEntity::getName)
                    .collect(Collectors.toList()));
            userGetDtos.add(userGetDto);
        }

        return userGetDtos;
    }
}
