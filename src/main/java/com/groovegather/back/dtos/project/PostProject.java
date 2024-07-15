package com.groovegather.back.dtos.project;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class PostProject {
    private String name;
    private String description;
    private String color;
    private Integer likes;
    private LocalDate date;
    private List<String> genres;
    private List<String> skillsPresent;
    private List<String> skillsMissing;
    private List<String> audioFiles;
    private List<String> scoreFiles;
    private List<String> archiveFiles;
    private String previewSong;
}