package com.groovegather.back.entities;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class ManageId implements Serializable {

    private Long projectId;

    private Long fileId;

    public ManageId(Long projectId, Long fileId) {
        this.projectId = projectId;
        this.fileId = fileId;
    }
}
