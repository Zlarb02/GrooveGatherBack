package com.groovegather.back.entities;

import java.sql.Timestamp;

import org.springframework.data.annotation.LastModifiedDate;

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
    private Integer role;

    @Column
    private String operation;

    @Column
    private String operationContent;

}
