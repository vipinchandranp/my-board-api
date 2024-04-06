package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Route;
import com.myboard.userservice.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RouteRepository extends MongoRepository<Route, String> {
    List<Route> findByRouteOwner(User loggedInUser);
}
