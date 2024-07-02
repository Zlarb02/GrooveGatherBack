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
@Table(name = "skill")
public class SkillEntity {
    @Id
    private String name;

    @Column(nullable = false)
    private Boolean isMissing;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<ProjectEntity> projects;

}
