package com.groovegather.back.entities;

import java.time.LocalDate;

import org.springframework.data.annotation.LastModifiedDate;

import com.groovegather.back.enums.OperateEnum;
import com.groovegather.back.enums.OperateRoleEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "operate")
public class OperateEntity {

    @EmbeddedId
    private OperateId id;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("userId")
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("projectId")
    private ProjectEntity project;

    @Column
    @LastModifiedDate
    private LocalDate localDate;

    @Column
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private OperateRoleEnum role;

    @Column
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private OperateEnum operation;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String operationContent;

    public OperateEntity(OperateEnum operation, ProjectEntity project, UserEntity user, OperateRoleEnum role) {
        this.id = new OperateId(user.getId(), project.getId());
        this.operation = operation;
        this.project = project;
        this.user = user;
        this.localDate = LocalDate.now();
        this.role = role;
    }

}
