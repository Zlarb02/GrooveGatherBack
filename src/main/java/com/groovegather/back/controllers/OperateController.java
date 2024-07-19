package com.groovegather.back.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groovegather.back.config.JWTService;
import com.groovegather.back.dtos.operate.OperateDto;
import com.groovegather.back.services.OperateService;

@RestController
@RequestMapping("api/v1/operations")
public class OperateController {

    private static OperateService operateService;

    public OperateController(OperateService operateService, JWTService jwtService) {
        OperateController.operateService = operateService;
    }

    @GetMapping("/user-projects")
    public ResponseEntity<Collection<OperateDto>> getUserProjects(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<OperateDto> operations = operateService.getUserProjects(userDetails);
            return ResponseEntity.ok(operations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
