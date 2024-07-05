package com.groovegather.back.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProjectSkillId implements Serializable {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "skill_name")
    private String skillName;
}
