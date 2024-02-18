package com.myboard.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myboard.userservice.service.PlayService;

@RestController
@RequestMapping("v1/play/")
@CrossOrigin
public class PlayController {

	@Autowired
	private PlayService playService;

	@GetMapping("/current-time-image")
	public ResponseEntity<?> getDisplayImageForCurrentTime() {
		byte[] displayImage = playService.getDisplayImageForCurrentTime();
		if (displayImage != null) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(displayImage);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No display image found for the current time.");
		}
	}

}
