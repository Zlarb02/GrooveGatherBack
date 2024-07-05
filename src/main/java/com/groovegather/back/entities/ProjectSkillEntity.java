package com.groovegather.back.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_skill")
public class ProjectSkillEntity {

    @EmbeddedId
    private ProjectSkillId id;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("projectId")
    private ProjectEntity project;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("skillName")
    private SkillEntity skill;

    @Column(name = "is_missing")
    private Boolean isMissing;

    public ProjectSkillEntity(ProjectEntity project, SkillEntity skill, Boolean isMissing) {
        this.project = project;
        this.skill = skill;
        this.isMissing = isMissing;
        this.id = new ProjectSkillId(project.getId(), skill.getName());
    }

}
