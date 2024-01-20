package com.myboard.userservice.dto;

import java.util.List;

public class MapsPlaceDTO {
	private String businessStatus;
	private String formattedAddress;
	private GeometryDTO geometry;
	private String icon;
	private String iconBackgroundColor;
	private String iconMaskBaseUri;
	private String name;
	private OpeningHoursDTO openingHours;
	private List<PhotoDTO> photos;
	private String placeId;
	private PlusCodeDTO plusCode;
	private double rating;
	private String reference;
	private List<String> types;
	private int userRatingsTotal;

	// Constructors, getters, and setters

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public GeometryDTO getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryDTO geometry) {
		this.geometry = geometry;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconBackgroundColor() {
		return iconBackgroundColor;
	}

	public void setIconBackgroundColor(String iconBackgroundColor) {
		this.iconBackgroundColor = iconBackgroundColor;
	}

	public String getIconMaskBaseUri() {
		return iconMaskBaseUri;
	}

	public void setIconMaskBaseUri(String iconMaskBaseUri) {
		this.iconMaskBaseUri = iconMaskBaseUri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OpeningHoursDTO getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(OpeningHoursDTO openingHours) {
		this.openingHours = openingHours;
	}

	public List<PhotoDTO> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoDTO> photos) {
		this.photos = photos;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public PlusCodeDTO getPlusCode() {
		return plusCode;
	}

	public void setPlusCode(PlusCodeDTO plusCode) {
		this.plusCode = plusCode;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public int getUserRatingsTotal() {
		return userRatingsTotal;
	}

	public void setUserRatingsTotal(int userRatingsTotal) {
		this.userRatingsTotal = userRatingsTotal;
	}

	public static class GeometryDTO {
		private LocationDTO location;
		private ViewportDTO viewport;

		public LocationDTO getLocation() {
			return location;
		}

		public void setLocation(LocationDTO location) {
			this.location = location;
		}

		public ViewportDTO getViewport() {
			return viewport;
		}

		public void setViewport(ViewportDTO viewport) {
			this.viewport = viewport;
		}

		// Constructors, getters, and setters
	}

	public static class LocationDTO {
		private double lat;
		private double lng;

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		// Constructors, getters, and setters
	}

	public static class ViewportDTO {
		private LocationDTO northeast;
		private LocationDTO southwest;

		public LocationDTO getNortheast() {
			return northeast;
		}

		public void setNortheast(LocationDTO northeast) {
			this.northeast = northeast;
		}

		public LocationDTO getSouthwest() {
			return southwest;
		}

		public void setSouthwest(LocationDTO southwest) {
			this.southwest = southwest;
		}

		// Constructors, getters, and setters
	}

	public static class OpeningHoursDTO {
		private boolean openNow;

		public boolean isOpenNow() {
			return openNow;
		}

		public void setOpenNow(boolean openNow) {
			this.openNow = openNow;
		}

		// Constructors, getters, and setters
	}

	public static class PhotoDTO {
		private int height;
		private List<String> htmlAttributions;
		private String photoReference;
		private int width;

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public List<String> getHtmlAttributions() {
			return htmlAttributions;
		}

		public void setHtmlAttributions(List<String> htmlAttributions) {
			this.htmlAttributions = htmlAttributions;
		}

		public String getPhotoReference() {
			return photoReference;
		}

		public void setPhotoReference(String photoReference) {
			this.photoReference = photoReference;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		// Constructors, getters, and setters
	}

	public static class PlusCodeDTO {
		private String compoundCode;
		private String globalCode;

		public String getCompoundCode() {
			return compoundCode;
		}

		public void setCompoundCode(String compoundCode) {
			this.compoundCode = compoundCode;
		}

		public String getGlobalCode() {
			return globalCode;
		}

		public void setGlobalCode(String globalCode) {
			this.globalCode = globalCode;
		}

		// Constructors, getters, and setters
	}
}
