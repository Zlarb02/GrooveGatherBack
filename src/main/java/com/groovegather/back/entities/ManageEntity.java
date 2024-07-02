package com.groovegather.back.entities;

import java.sql.Timestamp;

import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Entity;
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
@Table(name = "manage")
public class ManageEntity {

    @ManyToOne
    private ProjectEntity project;

    @ManyToOne
    private FileEntity file;

    @Id
    @LastModifiedDate
    private Timestamp timestamp;

}
