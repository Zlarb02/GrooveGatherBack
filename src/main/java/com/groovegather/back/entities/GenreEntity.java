package com.groovegather.back.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class GenreEntity {
    @Id
    private String name;
}
