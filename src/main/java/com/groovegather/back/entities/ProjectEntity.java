package com.groovegather.back.entities;

import java.util.ArrayList;
import java.util.List;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, length = 200)
    private String color;

    @Column(nullable = false)
    private Integer likes = 0;

    @ManyToMany
    @JoinTable(name = "project_genre", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "genre_name"))
    private List<GenreEntity> genres = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<OperateEntity> userProjectOperations = new ArrayList<>();

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private List<ManageEntity> projectManageFiles = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProjectSkillEntity> projectSkills = new ArrayList<>();

    public ProjectEntity(Object id, String name, String description, String color, int likes,
            ArrayList<GenreEntity> genres, ArrayList<OperateEntity> userProjectOperations,
            ArrayList<ManageEntity> projectManageFiles, ArrayList<ProjectSkillEntity> projectSkills) {
        this.id = (Long) id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.likes = likes;
        this.genres = genres;
        this.userProjectOperations = userProjectOperations;
        this.projectManageFiles = projectManageFiles;
        this.projectSkills = projectSkills;
    }
}
