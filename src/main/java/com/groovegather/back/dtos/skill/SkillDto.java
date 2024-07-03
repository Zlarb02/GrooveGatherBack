package com.groovegather.back.dtos.skill;

import lombok.Data;

@Data
public class SkillDto {
    private String name;

    public SkillDto(String name) {
        this.name = name;
    }
}