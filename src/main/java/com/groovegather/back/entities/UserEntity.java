package com.groovegather.back.entities;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 200)
    @Transient
    private String repeatedPassword;

    @Column(nullable = false, length = 200)
    private String email;

    @Column(nullable = false, length = 2000)
    private String picture;

    @Column(nullable = false, length = 2000)
    private String token;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Integer role;

    @Column(nullable = false)
    private Integer subscriptionLevel;

    @Column(nullable = false)
    private Boolean isGoogle;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "user_genre", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "genre_name"))
    private Collection<GenreEntity> genres = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.EAGER)
    private Collection<OperateEntity> userProjectOperations = new ArrayList<>();

}
