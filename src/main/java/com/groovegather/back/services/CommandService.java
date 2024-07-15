package com.groovegather.back.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class CommandService {

  private final Path soundFilesDir = Paths.get(System.getProperty("java.io.tmpdir"), "soundFiles");
  private final Set<String> supportedAudioTypes = new HashSet<>(
      Arrays.asList("audio/wav", "audio/x-wav", "audio/flac", "audio/mpeg"));

  public CommandService() throws Exception {
    // Créer le répertoire pour les fichiers audio s'il n'existe pas
    Files.createDirectories(soundFilesDir);
  }

  public File convertToMp3(File audioFile) throws Exception {
    // Vérifier le type de fichier audio
    if (!isSupportedAudioType(getContentType(audioFile))) {
      throw new IllegalArgumentException("Unsupported audio type.");
    }

    // Déterminer le nom du fichier MP3 de sortie
    String mp3FileName = audioFile.getName().replaceAll("\\.[a-zA-Z0-9]+$", ".mp3");
    Path mp3FilePath = soundFilesDir.resolve(mp3FileName);

    // Vérifier si le fichier d'entrée est déjà au format MP3
    if (audioFile.getName().toLowerCase().endsWith(".mp3")) {
      // Copier directement le fichier MP3 sans conversion
      Files.copy(audioFile.toPath(), mp3FilePath);
      return mp3FilePath.toFile();
    }

    // Construire la commande ffmpeg pour la conversion en MP3
    ProcessBuilder processBuilder = new ProcessBuilder(
        "ffmpeg", "-y", "-i", audioFile.getAbsolutePath(), "-codec:a", "libmp3lame", "-b:a", "64k",
        mp3FilePath.toString());

    // Démarrer le processus ffmpeg
    Process process = processBuilder.start();

    // Capturer et afficher les erreurs éventuelles de ffmpeg
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    }

    // Attendre que le processus ffmpeg se termine
    int exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new RuntimeException("Conversion failed with exit code " + exitCode);
    }

    return mp3FilePath.toFile();
  }

  public boolean isSupportedAudioType(String contentType) {
    return supportedAudioTypes.contains(contentType);
  }

  private String getContentType(File file) {
    // Simule la récupération du type MIME du fichier
    // Vous devez implémenter cette méthode selon la logique de votre application
    // Par exemple, utiliser Tika ou une autre bibliothèque pour obtenir le type
    // MIME
    // À titre d'exemple, retourne toujours "audio/mpeg" pour le démonstration
    return "audio/mpeg";
  }

  public Path getSoundFilesDir() {
    return soundFilesDir;
  }
}
