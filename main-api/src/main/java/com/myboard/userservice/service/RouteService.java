package com.myboard.userservice.service;

import com.myboard.userservice.dto.RouteDTO;

import java.util.List;

public interface RouteService {
    RouteDTO saveRoute(RouteDTO routeDTO);
    List<RouteDTO> getAllRoutes();
    // Add other service methods as needed
    List<RouteDTO> getRoutesForOwner();
    RouteDTO getRoute(String routeId);
}
