package com.groovegather.back.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groovegather.back.dtos.user.GetUser;
import com.groovegather.back.dtos.user.UserPostDto;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepo userRepo;

    private GenreRepo genreRepo;

    private static UserService userService;

    public UserController(UserRepo userRepo, GenreRepo genreRepo, UserService userService) {
        this.userRepo = userRepo;
        this.genreRepo = genreRepo;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<Collection<GetUser>> getAll() {
        Collection<GetUser> users = UserController.userService.getAll();

        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserPostDto> createUser(@RequestBody UserPostDto userPostDto) {
        UserPostDto createdUser = userService.createUser(userPostDto);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody UserEntity user, @PathVariable Long id) {
        try {
            Optional<UserEntity> opUser = this.userRepo.findById(id);
            if (opUser.isPresent()) {
                user.setId(id);
                this.userRepo.save(user);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.userRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
