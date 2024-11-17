package com.myboard.userservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileStorageService {

    @Value("${myboard.display.path}")
    private String displayPath;

    public String storeFile(MultipartFile file, String subdirectory) throws IOException {
        // Generate a unique file name
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Define the file path
        Path filePath = Paths.get(displayPath, subdirectory, uniqueFileName);

        // Ensure directories exist
        Files.createDirectories(filePath.getParent());

        // Write the file to the directory
        Files.write(filePath, file.getBytes());

        return filePath.toString();
    }

    public void deleteFile(String fileName, String subdirectory) throws IOException {
        Path filePath = Paths.get(displayPath, subdirectory, fileName);
        Files.deleteIfExists(filePath);
    }
}
