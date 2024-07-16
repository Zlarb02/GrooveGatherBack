package com.groovegather.back.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groovegather.back.dtos.user.GetUserDto;
import com.groovegather.back.dtos.user.UserDto;
import com.groovegather.back.errors.LoginException;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(value = "http://localhost:4200")
public class UserController {

    private final UserRepo userRepo;

    private static UserService userService;

    public UserController(UserRepo userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Collection<GetUserDto>> getAll() {
        Collection<GetUserDto> users = UserController.userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getOne(
            @RequestParam(value = "email") String email) {
        try {
            UserDto user = userService.getByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto, HttpServletResponse response) {
        try {
            UserDto createdUser = userService.createUser(userDto);
            return ResponseEntity.ok(createdUser);
        } catch (LoginException e) {
            throw new LoginException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletResponse response, Boolean isGoogle) {
        try {
            userService.logUser(userDto, response, isGoogle);
            return ResponseEntity.ok().build();
        } catch (LoginException e) {
            throw new LoginException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody UserDto user, @PathVariable Long id) {

        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<UserDto> patch(@RequestBody UserDto userDto, @RequestParam(value = "id") Long id) {
        try {
            UserDto user = userService.getById(id);
            user = userService.patchUser(user, userDto);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(value = "email") String email) {
        this.userRepo.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }
}
