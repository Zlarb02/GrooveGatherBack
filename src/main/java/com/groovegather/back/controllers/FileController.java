package com.groovegather.back.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.groovegather.back.services.CommandService;

@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin(value = "*")
public class FileController {
    @Autowired
    private CommandService commandService;

    @PostMapping("/convert")
    public ResponseEntity<Map<String, String>> convertToMp3(@RequestParam("file") MultipartFile file) throws Exception {
        if (!commandService.isSupportedAudioType(file.getContentType())) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(Map.of("error", "Unsupported audio type."));
        }

        // Sauvegarder le fichier sur le disque
        Path audioFilePath = commandService.getSoundFilesDir().resolve(file.getOriginalFilename());
        file.transferTo(audioFilePath.toFile());

        // Convertir le fichier audio en MP3
        File mp3File = commandService.convertToMp3(audioFilePath.toFile());

        // Retourner les liens de téléchargement pour les fichiers WAV et MP3
        Map<String, String> r = Map.of("wavUrl",
                String.format("/api/v1/files/download?filename=%s", file.getOriginalFilename()),
                "mp3Url", String.format("/api/v1/files/download?filename=%s", mp3File.getName()));

        return ResponseEntity.ok(r);
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
}