package com.groovegather.back.dtos.user;

import java.util.List;

import lombok.Data;

@Data
public class UserPostDto {
    private String name;
    private String password;
    private String email;
    private String picture;
    private String token;
    private String description;
    private Integer role;
    private Integer subscriptionLevel;
    private List<String> genres; // Assuming genre names are sent as strings
}
