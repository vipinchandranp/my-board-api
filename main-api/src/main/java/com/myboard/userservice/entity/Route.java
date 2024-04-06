package com.myboard.userservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "routes")
public class Route {
	@Id
	private String id;
	private String name;
	@DBRef
	private User routeOwner;
	private List<RoutePoints> routePointsList ;
}
