package com.groovegather.back.dtos.user;

import java.util.Collection;

import com.groovegather.back.enums.UserRoleEnum;

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
        UserRoleEnum role;
        Integer subscriptionLevel;
        Collection<String> genres;// Assuming genre names are sent as strings
        Boolean isGoogle;
}
