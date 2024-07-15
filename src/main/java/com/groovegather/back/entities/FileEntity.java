package com.groovegather.back.entities;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "file")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean isPrivate;

    @Column(nullable = false)
    private Boolean isScore;

    @Column(nullable = false)
    private Boolean isTeaser;

    @Column(nullable = false)
    private String fileExtension;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = true)
    private String convertedFileUrl; // URL du fichier converti

    @OneToMany(mappedBy = "file", cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    private Collection<ManageEntity> projectManageFiles = new ArrayList<>();
}
