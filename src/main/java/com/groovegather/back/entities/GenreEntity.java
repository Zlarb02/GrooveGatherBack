package com.groovegather.back.entities;

import java.util.List;

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
    private String name;

    @ManyToMany(mappedBy = "genres")
    private List<UserEntity> users;

    @ManyToMany(mappedBy = "genres")
    private List<ProjectEntity> projects;

}
