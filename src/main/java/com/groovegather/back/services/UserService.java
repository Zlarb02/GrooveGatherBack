package com.groovegather.back.services;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.groovegather.back.config.JWTService;
import com.groovegather.back.dtos.user.GetUserDto;
import com.groovegather.back.dtos.user.UserDto;
import com.groovegather.back.entities.GenreEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.errors.LoginException;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.dtoMappers.UserDtoMapper;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepo userRepo;

    private final GenreRepo genreRepo;

    private final UserDtoMapper userDtoMapper;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, UserDtoMapper userDtoMapper, GenreRepo genreRepo,
                       AuthenticationManager authenticationManager, JWTService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userDtoMapper = userDtoMapper;
        this.genreRepo = genreRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(UserDto userPostDto) throws LoginException {

        this.validatePassword(userPostDto.getPassword());

        UserEntity userEntity = userDtoMapper.toUserEntity(userPostDto);

        Boolean userAlreadyExist = isUserAlreadyExist(userPostDto);
        if (userAlreadyExist.equals(Boolean.FALSE)) {
            System.out.println(userEntity);
            userRepo.save(userEntity);
            return userDtoMapper.toUserDto(userEntity);
        } else if (userAlreadyExist.equals(Boolean.FALSE)
                && !userEntity.getPassword().equals(userPostDto.getRepeatedPassword())) {
            throw new LoginException("Les mots de passes ne sont pas identiques.");
        } else {
            throw new LoginException("L'adresse email est déja utilisé pour un compte enregistré.");
        }
    }

    public HttpServletResponse logUser(UserDto userPostDto, HttpServletResponse response) throws LoginException {

        try {
            final Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userPostDto.getEmail(), userPostDto.getPassword()));

            if (authenticate.isAuthenticated()) {
                Map<String, String> token = this.jwtService
                        .generateToken(((UserEntity) authenticate.getPrincipal()).getEmail());
                ResponseCookie cookie = ResponseCookie.from("token", token.get("Bearer"))
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(7 * 24 * 60 * 60)
                        .build();
                response.addHeader("set-cookie", cookie.toString());
            }
        } catch (BadCredentialsException b) {
            throw new BadCredentialsException("Je ne vous connais pas!!");
        }
        return response;
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

    public UserDto getByEmail(String email) {
        Optional<UserEntity> user = userRepo.findByEmail(email);
        return userDtoMapper.toUserDto(user.orElse(null));
    }

    public boolean isUserAlreadyExist(UserDto userPostDto) {
        Optional<UserEntity> user = userRepo.findByEmail(userPostDto.getEmail());
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