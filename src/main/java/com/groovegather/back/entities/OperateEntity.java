package com.groovegather.back.entities;

import java.sql.Timestamp;

import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private ProjectEntity project;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
