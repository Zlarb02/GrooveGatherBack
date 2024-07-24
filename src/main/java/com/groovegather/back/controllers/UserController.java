package com.groovegather.back.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groovegather.back.config.JWTService;
import com.groovegather.back.dtos.user.GetUserDto;
import com.groovegather.back.dtos.user.UserDto;
import com.groovegather.back.errors.LoginException;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepo userRepo;

    private static UserService userService;

    private static JWTService jwtService;

    public UserController(UserRepo userRepo, UserService userService, JWTService jwtService) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.jwtService = jwtService;
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

    @PatchMapping
    public ResponseEntity<?> patch(@RequestBody UserDto userDto, @RequestParam(value = "id") Long id) {
        try {
            UserDto user = userService.getById(id);
            user = userService.patchUser(user, userDto);
            return ResponseEntity.ok(user);
        } catch (LoginException e) {
            throw new LoginException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserByEmail(@RequestParam String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = authentication.getName(); // Assumes email is the principal

        if (!email.equals(authenticatedUserEmail)) {
            throw new LoginException("You can only delete your own account.");
        }

        userService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }
}
