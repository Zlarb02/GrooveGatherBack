package com.groovegather.back.dto.project;

import lombok.Data;

@Data
public class SkillDto {
    private String name;
    private Boolean isMissing;

    public SkillDto(String name) {
        this.name = name;
    }
}