package com.myboard.userservice.service;

import com.myboard.userservice.entity.User;
import com.myboard.userservice.service.MBUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Autowired
    private MBUserDetailsService mbUserDetailsService;

    @Value("${myboard.board.path}")
    private String boardPath;

    @Value("${myboard.display.path}")
    private String displayPath;

    public ResponseEntity<Resource> getBoardFile(String filename) {
        Path filePath = Paths.get(boardPath, "v", filename);
        return serveFile(filePath, filename);
    }

    public ResponseEntity<Resource> getDisplayFile(String filename) {
        Path filePath = Paths.get(displayPath, "v", filename);
        return serveFile(filePath, filename);
    }

    // Common logic for serving files
    private ResponseEntity<Resource> serveFile(Path filePath, String filename) {
        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                MediaType mediaType = determineMediaType(filename);
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Helper method to determine the media type based on file extension
    private MediaType determineMediaType(String filename) {
        String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "mp4":
                return MediaType.APPLICATION_OCTET_STREAM; // You can set the correct media type for video files
            // Add more cases as needed
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // Fallback type
        }
    }
}
