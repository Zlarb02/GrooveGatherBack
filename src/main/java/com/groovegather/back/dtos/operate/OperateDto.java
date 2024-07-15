package com.groovegather.back.dtos.operate;

import java.sql.Timestamp;

import com.groovegather.back.enums.OperateEnum;
import com.groovegather.back.enums.OperateRoleEnum;

public record OperateDto(
        Timestamp timestamp,
        OperateRoleEnum role,
        OperateEnum operation,
        String operationContent) {

}
