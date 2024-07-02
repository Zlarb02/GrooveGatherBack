package com.groovegather.back.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "genre")
public class GenreEntity {

    @Id
    @Column(name = "genre_name")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private List<UserEntity> users;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private List<ProjectEntity> projects;

    public GenreEntity(String name) {
        this.name = name;
    }
}
