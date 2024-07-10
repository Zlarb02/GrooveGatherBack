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

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(value = "*")
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

    @GetMapping("/google")
    public ResponseEntity<UserDto> getGoogleUserByEmail(
            @RequestParam(value = "email") String email) {
        try {
            UserDto user = userService.getGoogleUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto,
            @RequestParam(value = "isGoogle", defaultValue = "false") boolean isGoogle,
            @RequestParam(value = "isLogin", defaultValue = "false") boolean isLogin) {
        try {
            userDto.setIsGoogle(isGoogle);
            if (isLogin == true) {
                UserDto logUser = userService.logUser(userDto, isLogin);
                return ResponseEntity.ok(logUser);
            } else {
                UserDto createdUser = userService.createUser(userDto);
                return ResponseEntity.ok(createdUser);
            }
        } catch (

        LoginException e) {
            throw new LoginException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody UserDto user, @PathVariable Long id) {

        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<UserDto> patch(@RequestBody UserDto userDto, @RequestParam(value = "id") Long id) {
        try {
            System.out.println("hello");
            UserDto user = userService.getById(id);
            System.out.println(user);
            user = userService.patchUser(user, userDto);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.userRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
