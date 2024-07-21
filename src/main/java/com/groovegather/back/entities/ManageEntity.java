package com.groovegather.back.entities;

import java.time.LocalDate;

import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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

    @EmbeddedId
    private ManageId id;

    @ManyToOne
    @JoinColumn(name = "projectId", insertable = false, updatable = false)
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "fileId", insertable = false, updatable = false)
    private FileEntity file;

    @LastModifiedDate
    private LocalDate localDate;

    public ManageEntity(ProjectEntity project, FileEntity file) {
        this.id = new ManageId(project.getId(), file.getId());
        this.project = project;
        this.file = file;
        this.localDate = LocalDate.now();
    }

}
