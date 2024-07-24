package com.groovegather.back.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groovegather.back.dtos.operate.ParticipationRequestDto;
import com.groovegather.back.dtos.operate.RequestToBeAdmin;
import com.groovegather.back.dtos.project.GetProject;
import com.groovegather.back.dtos.project.PostProject;
import com.groovegather.back.entities.MessageEntity;
import com.groovegather.back.entities.OperateEntity;
import com.groovegather.back.entities.ProjectEntity;
import com.groovegather.back.entities.UserEntity;
import com.groovegather.back.enums.OperateEnum;
import com.groovegather.back.enums.OperateRoleEnum;
import com.groovegather.back.repositories.OperateRepo;
import com.groovegather.back.repositories.ProjectRepo;
import com.groovegather.back.services.MessageService;
import com.groovegather.back.services.ProjectService;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private OperateRepo operateRepo;

    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<Collection<GetProject>> getAll() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/filter")
    public ResponseEntity<Collection<GetProject>> getFilteredAndSortedProjects(
            @RequestParam Optional<String> genre,
            @RequestParam Optional<String> skills,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> direction) {

        Optional<List<String>> skillList = skills.map(s -> List.of(s.split(",")));

        return ResponseEntity.ok(projectService.getFilteredAndSortedProjects(genre, skillList, sortBy, direction));
    }

    @GetMapping("/{name}")
    public ResponseEntity<PostProject> getByName(@PathVariable String name) {
        return ResponseEntity.ok(this.projectService.getByName(name));
    }

    @PostMapping
    public ResponseEntity<PostProject> createProject(@RequestBody PostProject projectPostDto,
            @AuthenticationPrincipal UserDetails userDetails) throws SQLException {
        PostProject createdProject = projectService.createProject(projectPostDto, userDetails);
        return ResponseEntity.ok(createdProject);
    }

    @PutMapping("/edit/{name}")
    public ResponseEntity<?> update(@RequestBody ProjectEntity project, @PathVariable String name) {
        try {
            Optional<ProjectEntity> opProject = this.projectRepo.findByName(name);
            if (opProject.isPresent()) {
                project.setName(name);
                this.projectRepo.save(project);
                return ResponseEntity.ok(project);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/request-participation")
    public ResponseEntity<Map<String, String>> requestParticipation(@RequestBody RequestToBeAdmin request,
            @AuthenticationPrincipal UserEntity sender) {

        // Récupère le projet basé sur le nom
        Optional<ProjectEntity> optionalProject = projectRepo.findByName(request.getProjectName());

        if (optionalProject.isPresent()) {
            ProjectEntity project = optionalProject.get();

            // Trouver l'utilisateur receveur à partir de la table 'operate' où le rôle est
            // 'OWNER'
            Optional<OperateEntity> operateOpt = operateRepo.findByProjectAndRole(project, OperateRoleEnum.OWNER)
                    .flatMap(list -> list.stream().findFirst()); // Convertit la liste en stream et prend le premier
                                                                 // élément

            if (operateOpt.isPresent()) {
                UserEntity receiver = operateOpt.get().getUser();

                String baseContent = String.format(
                        "L'utilisateur %s souhaite participer au projet %s.",
                        sender.getUsername(), project.getName());

                MessageEntity message = new MessageEntity();
                message.setSender(sender);
                message.setReceiver(receiver);
                message.setContent(baseContent);
                message.setTimestamp(LocalDateTime.now());
                message.setProject(project); // Associe le message au projet
                messageService.saveMessage(message);

                Long messageId = message.getId();
                String content = String.format(
                        "L'utilisateur %s souhaite participer à votre projet %s. Voulez-vous approuver cette demande ?<br><br>"
                                + "<button data-action='accept' data-message-id='%d'></button> "
                                + "<button data-action='reject' data-message-id='%d'></button>",
                        sender.getUsername(), project.getName(), messageId, messageId);

                message.setContent(content);
                messageService.updateMessage(message);

                return ResponseEntity.ok(Map.of("message", "Participation request sent."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Owner not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Project not found"));
        }
    }

    @PutMapping("/response")
    public ResponseEntity<Map<String, String>> respondToParticipationRequest(
            @RequestBody ParticipationRequestDto requestDto,
            @AuthenticationPrincipal UserEntity receiver) {

        try {
            // Récupérer le message par son ID
            MessageEntity message = messageService.getMessageById(requestDto.getMessageId());

            // Récupérer le projet associé au message
            ProjectEntity project = message.getProject();
            if (project == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Project not found"));
            }

            // Récupérer l'expéditeur du message
            UserEntity sender = message.getSender();
            if (sender == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Sender not found"));
            }

            String responseContent;
            if (requestDto.isAccepted()) {
                // Ajouter l'utilisateur comme ADMIN
                OperateEntity operateEntity = new OperateEntity(
                        OperateEnum.APPROVE,
                        project,
                        sender,
                        OperateRoleEnum.ADMIN);
                operateRepo.save(operateEntity);
                responseContent = String.format("Demande pour participer au projet %s acceptée !",
                        project.getName());
            } else {
                responseContent = String.format("Demande pour participer au projet %s rejetée.",
                        project.getName());
            }

            // Créer un message de réponse
            MessageEntity responseMessage = new MessageEntity();
            responseMessage.setSender(receiver);
            responseMessage.setReceiver(sender);
            responseMessage.setContent(responseContent);
            responseMessage.setTimestamp(LocalDateTime.now());
            responseMessage.setProject(project);
            messageService.saveMessage(responseMessage);

            return ResponseEntity.ok(Map.of("message", "Participation request response processed."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred"));
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> delete(@PathVariable String name) {
        this.projectRepo.deleteByName(name);
        return ResponseEntity.ok().build();
    }
}
