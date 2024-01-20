package com.myboard.userservice.dto;

import java.io.Serializable;

public class SelectLocationDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private double latitude;
	private double longitude;

	public SelectLocationDTO() {
	}

	public SelectLocationDTO(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// Getters and setters (omitted for brevity)

	@Override
	public String toString() {
		return "PlaceDto{" + "name='" + name + '\'' + ", latitude=" + latitude + ", longitude=" + longitude + '}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
