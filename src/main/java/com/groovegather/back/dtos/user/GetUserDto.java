package com.groovegather.back.dtos.user;

import java.util.Collection;

import lombok.Data;

@Data
public class GetUserDto {
        Long id;
        String name;
        String password;
        String email;
        String picture;
        String token;
        String description;
        Integer role;
        Integer subscriptionLevel;
        Collection<String> genres;// Assuming genre names are sent as strings
        Boolean isGoogle;
}
