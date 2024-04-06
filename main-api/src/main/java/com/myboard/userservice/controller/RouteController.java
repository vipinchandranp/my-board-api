package com.myboard.userservice.controller;

import com.myboard.userservice.dto.RouteDTO;
import com.myboard.userservice.service.RouteService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/route")
@CrossOrigin
public class RouteController {

	private final RouteService routeService;

	@Autowired
	public RouteController(RouteService routeService) {
		this.routeService = routeService;
	}

	@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RouteDTO> createRoute(@RequestBody RouteDTO routeDTO) {
		RouteDTO savedRouteDTO = routeService.saveRoute(routeDTO);
		return new ResponseEntity<>(savedRouteDTO, HttpStatus.CREATED);
	}

	@GetMapping("/allroutes")
	public ResponseEntity<List<RouteDTO>> getAllRoutesForLoggedinUser() {
		List<RouteDTO> routeDTOs = routeService.getRoutesForOwner();
		return new ResponseEntity<>(routeDTOs, HttpStatus.OK);
	}

	@GetMapping("/{routeId}")
	public ResponseEntity<RouteDTO> getRouteById(@PathVariable  String routeId) {
		RouteDTO routeDTO = routeService.getRoute(routeId);
		return new ResponseEntity<>(routeDTO, HttpStatus.OK);
	}
}
