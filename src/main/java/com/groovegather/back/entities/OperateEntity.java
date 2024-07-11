package com.groovegather.back.entities;

import java.sql.Timestamp;

import org.springframework.data.annotation.LastModifiedDate;

import com.groovegather.back.enums.OperateEnum;
import com.groovegather.back.enums.OperateRoleEnum;

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

    @ManyToOne
    @MapsId("userId")
    private UserEntity user;

    @ManyToOne
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

}
