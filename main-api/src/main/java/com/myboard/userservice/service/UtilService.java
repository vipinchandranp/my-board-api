package com.myboard.userservice.service;

import com.myboard.userservice.config.MailProperties;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.email.EmailRequest;
import com.myboard.userservice.types.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
public class UtilService {
    public MediaType determineMediaType(MultipartFile file) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        if (contentType != null) {
            if (contentType.startsWith("image") || isImageExtension(fileName)) {
                return MediaType.IMAGE;
            } else if (contentType.startsWith("video") || isVideoExtension(fileName)) {
                return MediaType.VIDEO;
            } else if (contentType.startsWith("audio")) {
                return MediaType.AUDIO;
            }
        }
        return MediaType.UNKNOWN; // For unsupported or unrecognized media types
    }

    private boolean isImageExtension(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff", "webp").contains(extension);
    }

    private boolean isVideoExtension(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return Arrays.asList("mp4", "avi", "mov", "wmv", "flv", "mkv", "webm").contains(extension);
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex > 0) ? fileName.substring(lastIndex + 1) : "";
    }

}
