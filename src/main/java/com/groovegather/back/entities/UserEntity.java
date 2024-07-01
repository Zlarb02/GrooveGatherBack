package com.groovegather.back.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 200)
    private String email;

    @Column(nullable = false, length = 2000)
    private String picture;

    @Column(nullable = false, length = 2000)
    private String token;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private Integer role;

    @Column(nullable = false)
    private Integer subscriptionLevel;

}
