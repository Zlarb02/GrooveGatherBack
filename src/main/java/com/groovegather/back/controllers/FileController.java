package com.groovegather.back.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.groovegather.back.dtos.project.FileDto;
import com.groovegather.back.entities.FileEntity;
import com.groovegather.back.repositories.FileRepo;
import com.groovegather.back.services.CommandService;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Autowired
    private CommandService commandService;

    @Autowired
    private FileRepo fileRepository;

    @PostMapping("/convert")
    public ResponseEntity<Map<String, String>> convertToMp3(@RequestParam("file") MultipartFile file) throws Exception {
        if (!commandService.isSupportedAudioType(file.getContentType())) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(Map.of("error", "Unsupported audio type."));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        String fileHash = calculateFileHash(file);
        FileEntity existingFile = fileRepository.findByFileHash(fileHash);
        if (existingFile != null) {
            String existingFilename = existingFile.getName();
            return ResponseEntity.ok(Map.of(
                    "originalUrl", String.format("files/download?filename=%s", existingFilename),
                    "mp3Url", existingFile.getUrl()));
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String uniqueFilename = userId + "_" + UUID.randomUUID().toString() + fileExtension;

        Path audioFilePath = commandService.getSoundFilesDir().resolve(uniqueFilename);
        file.transferTo(audioFilePath.toFile());

        File mp3File = commandService.convertToMp3(audioFilePath.toFile());
        Path mp3FilePath = mp3File.toPath(); // Obtenir le chemin du fichier MP3

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(originalFilename);
        fileEntity.setDescription("Converted file");
        fileEntity.setIsPrivate(false);
        fileEntity.setIsScore(false);
        fileEntity.setIsTeaser(fileHash.startsWith("teaser_")); // Logique isTeaser basée sur le hash
        fileEntity.setFileExtension(".mp3");
        fileEntity.setSize(Files.size(mp3FilePath));
        fileEntity.setUrl(String.format("files/download?filename=%s", mp3File.getName()));
        fileEntity.setFileHash(fileHash);

        fileRepository.save(fileEntity);

        Map<String, String> response = Map.of(
                "originalUrl", String.format("files/download?filename=%s", uniqueFilename),
                "mp3Url", String.format("files/download?filename=%s", mp3File.getName()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<List<FileDto>> uploadProjectFiles(@RequestParam("files") List<MultipartFile> files)
            throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<FileDto> fileDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileHash = calculateFileHash(file);
            FileEntity existingFile = fileRepository.findByFileHash(fileHash);
            if (existingFile != null) {
                fileDtos.add(new FileDto(
                        existingFile.getUrl(),
                        existingFile.getIsTeaser(),
                        existingFile.getName(),
                        existingFile.getSize()));
                continue;
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String uniqueFilename = userId + "_" + UUID.randomUUID().toString() + fileExtension;

            Path filePath = commandService.getSoundFilesDir().resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(originalFilename);
            fileEntity.setDescription("Uploaded file");
            fileEntity.setIsPrivate(false);
            fileEntity.setIsScore(false);
            fileEntity.setIsTeaser(fileHash.startsWith("teaser_")); // Logique isTeaser basée sur le hash
            fileEntity.setFileExtension(fileExtension);
            fileEntity.setSize(Files.size(filePath));
            fileEntity.setUrl(String.format("files/download?filename=%s", uniqueFilename));
            fileEntity.setFileHash(fileHash);

            fileRepository.save(fileEntity);

            fileDtos.add(new FileDto(
                    fileEntity.getUrl(),
                    fileEntity.getIsTeaser(),
                    originalFilename,
                    fileEntity.getSize()));
        }

        return ResponseEntity.ok(fileDtos);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename) throws Exception {
        Path filePath = commandService.getSoundFilesDir().resolve(filename);
        byte[] fileBytes = Files.readAllBytes(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(fileBytes);
    }

    private String calculateFileHash(MultipartFile file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = file.getBytes();
        byte[] hash = digest.digest(bytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
