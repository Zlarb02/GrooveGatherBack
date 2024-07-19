package com.groovegather.back.dtos.operate;

import java.time.LocalDate;

import com.groovegather.back.enums.OperateEnum;
import com.groovegather.back.enums.OperateRoleEnum;

import lombok.Data;

@Data
public class OperateDto {
        LocalDate localDate;
        OperateRoleEnum role;
        OperateEnum operation;
        String operationContent;
        String user;
        String project;
}
