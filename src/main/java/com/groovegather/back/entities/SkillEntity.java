package com.groovegather.back.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SkillEntity {
    @Id
    private String name;

    @Column(nullable = false)
    private Boolean isMissing;
}
