package com.groovegather.back.dtos.project;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class PostProject {
    private Long id;
    private String name;
    private String description;
    private String color;
    private Integer likes;
    private LocalDate date;
    private List<String> genres;
    private List<String> skillsPresent;
    private List<String> skillsMissing;
    private List<FileDto> files;
}