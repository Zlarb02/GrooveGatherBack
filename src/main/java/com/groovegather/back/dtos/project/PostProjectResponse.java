package com.groovegather.back.dtos.project;

import java.time.LocalDate;
import java.util.List;

public record PostProjectResponse(
        String name,
        String description,
        String color,
        Integer likes,
        LocalDate date,
        List<String> genres,
        List<String> skillsPresent,
        List<String> skillsMissing,
        String owner,
        List<String> admins,
        List<String> comments,
        List<String> audioFiles,
        List<String> scoreFiles,
        List<String> archiveFiles,
        String previewSong) {

}
