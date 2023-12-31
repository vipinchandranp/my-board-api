package com.myboard.userservice.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
