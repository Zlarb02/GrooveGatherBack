package com.groovegather.back.services;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.groovegather.back.dtos.project.FileDto;
import com.groovegather.back.dtos.project.GetProject;
import com.groovegather.back.dtos.project.PostProject;
import com.groovegather.back.entities.FileEntity;
import com.groovegather.back.entities.ManageEntity;
import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.enums.OperateEnum;
import com.groovegather.back.enums.OperateRoleEnum;
import com.groovegather.back.repositories.FileRepo;
import com.groovegather.back.repositories.GenreRepo;
import com.groovegather.back.repositories.ManageRepo;
import com.groovegather.back.repositories.OperateRepo;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.repositories.UserRepo;
import com.groovegather.back.services.dtoMappers.ProjectDtoMapper;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private OperateRepo operateRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProjectDtoMapper projectDtoMapper;

    @Autowired
    private ManageRepo manageRepo;

    @Autowired
    private FileRepo fileRepo;

    @Transactional
    public PostProject updateProject(String projectName, PostProject projectUpdateDto, UserDetails userDetails) {
        ProjectEntity projectEntity = projectRepo.findByName(projectName)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Update project details
        if (projectUpdateDto.getDescription() != null) {
            projectEntity.setDescription(projectUpdateDto.getDescription());
        }
        if (projectUpdateDto.getColor() != null) {
            projectEntity.setColor(projectUpdateDto.getColor());
        }

        // Handle file updates
        Set<Long> existingFileIds = projectEntity.getProjectManageFiles().stream()
                .map(manageEntity -> manageEntity.getFile().getId())
                .collect(Collectors.toSet());

        Set<String> updatedFileUrls = projectUpdateDto.getFiles().stream()
                .map(FileDto::getUrl)
                .collect(Collectors.toSet());

        // Add new files
        Collection<ManageEntity> manageEntities = new ArrayList<>();
        for (FileDto fileDto : projectUpdateDto.getFiles()) {
            if (!existingFileIds.contains(fileDto.getId())) {
                FileEntity fileEntity = fileRepo.findByUrl(fileDto.getUrl()).orElse(null);
                if (fileEntity == null) {
                    fileEntity = new FileEntity();
                    fileEntity.setUrl(fileDto.getUrl());
                    fileEntity.setName(fileDto.getUrl().substring(fileDto.getUrl().lastIndexOf("/") + 1));
                    fileEntity.setDescription("Description par défaut");
                    fileEntity.setIsPrivate(false);
                    fileEntity.setIsScore(false);
                    fileEntity.setIsTeaser(fileDto.getIsTeaser());
                    fileEntity
                            .setFileExtension('.' + fileDto.getUrl().substring(fileDto.getUrl().lastIndexOf(".") + 1));
                    fileEntity.setFileHash(HashUtil.generateSHA256Hash(fileDto.getUrl()));
                    fileEntity.setSize(0L);
                    fileEntity = fileRepo.save(fileEntity);
                }
                fileEntity.setIsTeaser(fileDto.getIsTeaser());
                ManageEntity manageEntity = new ManageEntity(projectEntity, fileEntity);
                manageEntities.add(manageEntity);
            }

            manageRepo.saveAll(manageEntities);

            projectEntity.setProjectManageFiles(manageEntities);
        }

        // Remove old files
        for (ManageEntity manageEntity : projectEntity.getProjectManageFiles()) {
            if (!updatedFileUrls.contains(manageEntity.getFile().getUrl())) {
                manageRepo.delete(manageEntity);
            }
        } // Remove old files
        for (ManageEntity manageEntity : projectEntity.getProjectManageFiles()) {
            if (!updatedFileUrls.contains(manageEntity.getFile().getUrl())) {
                manageRepo.delete(manageEntity);
            }
        }
        projectRepo.save(projectEntity);

        return projectDtoMapper.toProjectPostDto(projectEntity);
    }

    private UserEntity getCurrentUser(String username) {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public PostProject createProject(PostProject projectPostDto, UserDetails userDetails)
            throws SQLException {
        ProjectEntity projectEntity = projectDtoMapper.toProjectEntity(projectPostDto);
        UserEntity user = getCurrentUser(userDetails.getUsername());

        projectEntity = projectRepo.save(projectEntity);

        operateRepo.save(new OperateEntity(OperateEnum.CREATE, projectEntity, user, OperateRoleEnum.OWNER));

        Collection<ManageEntity> manageEntities = new ArrayList<>();
        for (FileDto fileDto : projectPostDto.getFiles()) {
            FileEntity fileEntity = fileRepo.findByUrl(fileDto.getUrl()).orElse(null);
            if (fileEntity == null) {
                FileEntity fileEntityNull = new FileEntity();
                fileEntityNull.setUrl(fileDto.getUrl());
                String fileName = fileDto.getUrl().substring(fileDto.getUrl().lastIndexOf("/") + 1);
                fileEntityNull.setName(fileName);
                fileEntityNull.setDescription("Description par défaut");
                fileEntityNull.setIsPrivate(false);
                fileEntityNull.setIsScore(false);
                fileEntityNull.setIsTeaser(fileDto.getIsTeaser());
                fileEntityNull
                        .setFileExtension('.' + fileDto.getUrl().substring(fileDto.getUrl().lastIndexOf(".") + 1));
                String fileHash = HashUtil.generateSHA256Hash(fileDto.getUrl());
                if (fileHash == null || fileHash.isEmpty()) {
                    throw new RuntimeException("File hash cannot be null or empty");
                }
                fileEntityNull.setFileHash(fileHash);
                fileEntityNull.setSize(new File(fileDto.getUrl()).length());
                fileEntity = fileRepo.save(fileEntityNull);
            }

            fileEntity.setIsTeaser(fileDto.getIsTeaser());
            ManageEntity manageEntity = new ManageEntity(projectEntity, fileEntity);
            manageEntities.add(manageEntity);
        }

        manageRepo.saveAll(manageEntities);

        projectEntity.setProjectManageFiles(manageEntities);

        return projectDtoMapper.toProjectPostDto(projectEntity);
    }

    public Collection<GetProject> getAllProjects() {
        Collection<ProjectEntity> projectEntities = projectRepo.findAll();
        return projectDtoMapper.toGetProjectsDto(projectEntities);
    }

    public Collection<GetProject> getFilteredAndSortedProjects(Optional<String> genre,
            Optional<List<String>> skills,
            Optional<String> sortBy,
            Optional<String> direction) {

        Collection<ProjectEntity> projects = projectRepo.findAll();

        // Filter by genre
        if (genre.isPresent()) {
            projects = projects.stream()
                    .filter(p -> p.getGenres().stream().anyMatch(g -> g.getName().equals(genre.get())))
                    .collect(Collectors.toList());
        }

        // Filter by skills
        if (skills.isPresent() && !skills.get().isEmpty()) {
            projects = projects.stream()
                    .filter(p -> p.getProjectSkills().stream()
                            .anyMatch(ps -> skills.get().contains(ps.getSkill().getName())))
                    .collect(Collectors.toList());
        }

        // Sort the projects
        if (sortBy.isPresent()) {
            String sortField = sortBy.get();
            boolean ascending = !direction.isPresent() || !direction.get().equalsIgnoreCase("desc");

            projects = projects.stream()
                    .sorted((p1, p2) -> {
                        int comparison = 0;
                        switch (sortField) {
                            case "date":
                                comparison = p1.getDate().compareTo(p2.getDate());
                                break;
                            case "likes":
                                comparison = Integer.compare(p1.getLikes(), p2.getLikes());
                                break;
                            // Add more sorting cases if necessary
                            default:
                                throw new IllegalArgumentException("Invalid sortBy parameter: " + sortField);
                        }
                        return ascending ? comparison : -comparison;
                    })
                    .collect(Collectors.toList());
        }

        return projectDtoMapper.toGetProjectsDto(projects);
    }

    public PostProject getByName(String name) {
        ProjectEntity projectEntity = projectRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return projectDtoMapper.toProjectPostDto(projectEntity);
    }
}
