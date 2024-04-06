package com.myboard.userservice.service;

import com.myboard.userservice.dto.RouteDTO;
import com.myboard.userservice.dto.RoutePointsDTO;
import com.myboard.userservice.entity.Route;
import com.myboard.userservice.entity.RoutePoints;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.RouteRepository;
import com.myboard.userservice.security.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;

    @Autowired
    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public RouteDTO saveRoute(RouteDTO routeDTO) {
        Route route = convertToEntity(routeDTO); // Convert RouteDTO to Route entity
        List<RoutePoints> routePointsList = routeDTO.getLocations().stream()
                .map(this::convertToEntity) // Convert each RoutePointsDTO to RoutePoints entity
                .collect(Collectors.toList());
        route.setRoutePointsList(routePointsList); // Set the list of RoutePoints in Route entity
        User loggedInUser = SecurityUtils.getLoggedInUser();
        route.setRouteOwner(loggedInUser);
        Route savedRoute = routeRepository.save(route); // Save the Route entity
        return convertToDTO(savedRoute); // Convert the saved Route entity back to RouteDTO
    }

    @Override
    public List<RouteDTO> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        return routes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteDTO> getRoutesForOwner() {
        User loggedInUser = SecurityUtils.getLoggedInUser();
        List<Route> routeList = routeRepository.findByRouteOwner(loggedInUser);

        // Convert Route entities to RouteDTOs
        List<RouteDTO> routeDTOList = new ArrayList<>();
        for (Route route : routeList) {
            RouteDTO routeDTO = new RouteDTO();
            // Set properties of RouteDTO from Route entity
            routeDTO.setId(route.getId());
            routeDTO.setName(route.getName());
            //routeDTO.setRouteOwner(route.getRouteOwner());
            // Assuming RoutePoints in RouteDTO has a similar structure as RoutePoints in Route entity
            List<RoutePointsDTO> routePointsDTOList = new ArrayList<>();
            for (RoutePoints routePoints : route.getRoutePointsList()) {
                RoutePointsDTO routePointsDTO = new RoutePointsDTO();
                routePointsDTO.setLatitude(routePoints.getLatitude());
                routePointsDTO.setLongitude(routePoints.getLongitude());
                routePointsDTOList.add(routePointsDTO);
            }
            routeDTO.setLocations(routePointsDTOList);

            routeDTOList.add(routeDTO);
        }

        return routeDTOList;
    }

    @Override
    public RouteDTO getRoute(String routeId) {
        // Retrieve the route from the repository by its ID
        Optional<Route> optionalRoute = routeRepository.findById(routeId);

        // Check if the route exists
        if (optionalRoute.isPresent()) {
            // Map the properties from the Route entity to the RouteDTO
            Route route = optionalRoute.get();
            RouteDTO routeDTO = new RouteDTO();
            routeDTO.setId(route.getId());
            routeDTO.setName(route.getName());
            routeDTO.setRouteOwner(route.getRouteOwner());

            // Assuming RoutePoints in RouteDTO has a similar structure as RoutePoints in Route entity
            List<RoutePointsDTO> routePointsDTOList = new ArrayList<>();
            for (RoutePoints routePoints : route.getRoutePointsList()) {
                RoutePointsDTO routePointsDTO = new RoutePointsDTO();
                routePointsDTO.setLatitude(routePoints.getLatitude());
                routePointsDTO.setLongitude(routePoints.getLongitude());
                routePointsDTOList.add(routePointsDTO);
            }
            routeDTO.setLocations(routePointsDTOList);

            return routeDTO;
        } else {
            // If the route with the given ID does not exist, return null or throw an exception
            return null;
        }
    }


    // Convert Route entity to RouteDTO
    private RouteDTO convertToDTO(Route route) {
        RouteDTO routeDTO = new RouteDTO();
        BeanUtils.copyProperties(route, routeDTO);
        return routeDTO;
    }

    // Convert RouteDTO to Route entity
    private Route convertToEntity(RouteDTO routeDTO) {
        Route route = new Route();
        BeanUtils.copyProperties(routeDTO, route);
        return route;
    }

    // Convert RoutePointsDTO to RoutePoints entity
    private RoutePoints convertToEntity(RoutePointsDTO routePointsDTO) {
        RoutePoints routePoints = new RoutePoints();
        BeanUtils.copyProperties(routePointsDTO, routePoints);
        return routePoints;
    }
}
