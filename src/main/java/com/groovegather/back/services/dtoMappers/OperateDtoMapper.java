package com.groovegather.back.services.dtoMappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.groovegather.back.dtos.operate.OperateDto;
import com.groovegather.back.entities.OperateEntity;

@Component
public class OperateDtoMapper {
    public List<OperateDto> toOperateDtos(List<OperateEntity> operateEntities) {
        List<OperateDto> operateDtos = new ArrayList<>();
        for (OperateEntity operateEntity : operateEntities) {
            OperateDto operateDto = new OperateDto();
            operateDto.setLocalDate(operateEntity.getLocalDate());
            operateDto.setRole(operateEntity.getRole());
            operateDto.setOperation(operateEntity.getOperation());
            operateDto.setOperationContent(operateEntity.getOperationContent());
            operateDto.setUser(operateEntity.getUser().getUsername());
            operateDto.setProject(operateEntity.getProject().getName());
            operateDtos.add(operateDto);
        }
        return operateDtos;
    };
}
