package com.groovegather.back.dtos.user;

import java.util.Collection;

import lombok.Data;

@Data
public class UserPostDto {
    private String name;
    private String password;
    private String repeatedPassword;
    private String email;
    private String picture;
    private String token;
    private String description;
    private Integer role;
    private Integer subscriptionLevel;
    private Collection<String> genres; // Assuming genre names are sent as strings
}
