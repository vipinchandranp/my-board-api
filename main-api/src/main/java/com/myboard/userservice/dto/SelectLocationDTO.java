package com.myboard.userservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectLocationDTO{
	private String name;
	private double latitude;
	private double longitude;
}
