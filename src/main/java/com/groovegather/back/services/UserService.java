package com.groovegather.back.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groovegather.back.entities.GenreEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GenreRepo genreRepo;

    @Transactional
    public UserEntity createUser(UserEntity userEntity) {
        List<GenreEntity> genres = new ArrayList<>();
        for (GenreEntity genre : userEntity.getGenres()) {
            GenreEntity existingGenre = genreRepo.findById(genre.getName()).orElse(null);
            if (existingGenre == null) {
                existingGenre = genreRepo.save(new GenreEntity(genre.getName()));
            }
            genres.add(existingGenre);
        }
        userEntity.setGenres(genres);
        return userRepo.save(userEntity);
    }
}
