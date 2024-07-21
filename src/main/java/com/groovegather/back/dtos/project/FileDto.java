package com.groovegather.back.dtos.project;


import lombok.Data;

@Data
public class FileDto {
    private String url;
    private Boolean isTeaser;
    private String name;
    private Long size;

    public FileDto(String url, Boolean isTeaser, String name, Long size) {
        this.url = url;
        this.isTeaser = isTeaser;
        this.name = name;
        this.size = size;
    }
}