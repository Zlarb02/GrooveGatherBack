package com.groovegather.back.entities;

import java.sql.Timestamp;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "operate")
public class OperateEntity {

    @EmbeddedId
    private OperateId id;

    @ManyToOne (cascade=CascadeType.ALL)
    @MapsId("userId")
    private UserEntity user;

    @ManyToOne (cascade=CascadeType.ALL)
    @MapsId("projectId")
    private ProjectEntity project;

    @Column
    @LastModifiedDate
    private Timestamp timestamp;

    @Column
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private OperateRoleEnum role;

    @Column
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private OperateEnum operation;


    @Column(nullable = true, columnDefinition = "TEXT")
    private String operationContent;

    public OperateEntity(OperateEnum operation, ProjectEntity project, UserEntity user){
        this.id = new OperateId(user.getId(), project.getId());
        this.operation = operation;
        this.project = project;
        this.user = user;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        }

}
