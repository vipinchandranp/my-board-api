package com.myboard.userservice.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "users")
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String username;

	private String password;

	private Map<String, List<Board>> approvalsRequiredMap = new HashMap<String, List<Board>>();

	private Set<String> roles = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<DisplayDetails> displays;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private Location location;

	@OneToOne(mappedBy = "user")
	private UserProfile userProfile;

	// Getters and setters for all fields

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<DisplayDetails> getDisplays() {
		return displays;
	}

	public void setDisplays(List<DisplayDetails> displays) {
		this.displays = displays;
	}

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(String username, String password, Set<String> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public Map<String, List<Board>> getApprovalsRequiredMap() {
		return approvalsRequiredMap;
	}

	public void setApprovalsRequiredMap(Map<String, List<Board>> approvalsRequiredMap) {
		this.approvalsRequiredMap = approvalsRequiredMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
