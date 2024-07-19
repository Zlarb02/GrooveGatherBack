package com.groovegather.back.entities;

import java.time.LocalDate;

import org.springframework.data.annotation.LastModifiedDate;

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
@Table(name = "manage")
public class ManageEntity {

    @EmbeddedId
    private ManageId id;

    @ManyToOne
    @MapsId("projectId")
    private ProjectEntity project;

    @ManyToOne
    @MapsId("fileId")
    private FileEntity file;

    @LastModifiedDate
    private LocalDate localDate;

}
