package com.groovegather.back.services.dtoMappers;

import org.springframework.stereotype.Component;

import com.groovegather.back.dtos.project.FileDto;
import com.groovegather.back.entities.FileEntity;

@Component
public class FileDtoMapper {

    public FileDto toFileDto(FileEntity fileEntity) {

        return new FileDto(fileEntity.getUrl(), fileEntity.getIsTeaser(), fileEntity.getName(),
                fileEntity.getSize());
    }
}