package com.groovegather.back.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groovegather.back.dtos.operate.OperateDto;
import com.groovegather.back.dtos.user.GetUserDto;
import com.groovegather.back.dtos.user.UserDto;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.services.OperateService;
import com.groovegather.back.services.dtoMappers.UserDtoMapper;

@RestController
@RequestMapping("api/v1/operations")
public class OperateController {

    private final OperateService operateService;

    private final UserDtoMapper userDtomapper;

    public OperateController(OperateService operateService, UserDtoMapper userDtomapper) {
        this.operateService = operateService;
        this.userDtomapper = userDtomapper;
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

    @GetMapping("/can-edit")
    public ResponseEntity<Boolean> canEditProject(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String projectName) {
        try {
            boolean canEdit = operateService.canEditProject(userDetails, projectName);
            return ResponseEntity.ok(canEdit);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/project-owner")
    public ResponseEntity<UserDto> getProjectOwner(@RequestParam String projectName) {
        try {
            UserEntity owner = operateService.getProjectOwner(projectName);
            return ResponseEntity.ok(userDtomapper.toUserDto(owner));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/admins")
    public ResponseEntity<Collection<GetUserDto>> getAdminsOfProject(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String projectName) {
        try {
            List<UserEntity> admins = operateService.getAdminsOfProject(userDetails, projectName);
            return ResponseEntity.ok(userDtomapper.toUsersDtos(admins));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/admin-projects")
    public ResponseEntity<Collection<OperateDto>> getProjectsWhereUserIsAdmin(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<OperateDto> projects = operateService.getProjectsWhereUserIsAdmin(userDetails);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
