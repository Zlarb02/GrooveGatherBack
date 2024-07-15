package com.groovegather.back.dtos.user;

import java.util.Collection;

import com.groovegather.back.enums.UserRoleEnum;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String password;
    private String repeatedPassword;
    private String email;
    private String picture;
    private String token;
    private String description;
    private UserRoleEnum role;
    private Integer subscriptionLevel;
    private Collection<String> genres; // Assuming genre names are sent as strings
    private Boolean isGoogle;
}
