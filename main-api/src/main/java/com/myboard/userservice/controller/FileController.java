package com.myboard.userservice.controller;

import com.myboard.userservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/file")
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/board/{filename:.+}")
    public ResponseEntity<Resource> getBoardFile(@PathVariable String filename) {
        return fileService.getBoardFile(filename);
    }

    @GetMapping("/display/{filename:.+}")
    public ResponseEntity<Resource> getDisplayFile(@PathVariable String filename) {
        return fileService.getDisplayFile(filename);
    }
}
