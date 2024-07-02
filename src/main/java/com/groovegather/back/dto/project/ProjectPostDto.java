package com.groovegather.back.dto.project;

import java.util.List;

import lombok.Data;

@Data
public class ProjectPostDto {
    private String name;
    private String description;
    private String color;
    private String likes;
    private List<String> genres;
    private List<SkillDto> skillsPresent;
    private List<SkillDto> skillsMissing;
}