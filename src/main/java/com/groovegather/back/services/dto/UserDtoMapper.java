package com.groovegather.back.services.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groovegather.back.dto.user.UserPostDto;
import com.groovegather.back.entities.GenreEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.repositories.GenreRepo;

@Component
public class UserDtoMapper {

    @Autowired
    private GenreRepo genreRepo;

    public UserEntity toUserEntity(UserPostDto userPostDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userPostDto.getName());
        userEntity.setPassword(userPostDto.getPassword());
        userEntity.setEmail(userPostDto.getEmail());
        userEntity.setPicture(userPostDto.getPicture());
        userEntity.setToken(userPostDto.getToken());
        userEntity.setDescription(userPostDto.getDescription());
        userEntity.setRole(userPostDto.getRole());
        userEntity.setSubscriptionLevel(userPostDto.getSubscriptionLevel());

        // Example of mapping genres (assuming genre names are sent as strings)
        List<GenreEntity> genres = new ArrayList<>();
        for (String genreName : userPostDto.getGenres()) {
            GenreEntity genreEntity = genreRepo.findByName(genreName);
            if (genreEntity == null) {
                genreEntity = new GenreEntity(genreName);
                genreEntity = genreRepo.save(genreEntity);
            }

            genres.add(genreEntity);
        }
        userEntity.setGenres(genres);

        return userEntity;
    }
}
