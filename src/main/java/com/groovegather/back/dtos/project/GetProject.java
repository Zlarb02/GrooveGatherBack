package com.groovegather.back.dtos.project;

import java.util.List;

import lombok.Data;

@Data
public class GetProject {
    private Long id;
    private String name;
    private String description;
    private String color;
    private Integer likes;
    private List<String> genres;
    private List<String> skillsPresent;
    private List<String> skillsMissing;
}
