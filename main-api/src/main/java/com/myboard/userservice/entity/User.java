package com.myboard.userservice.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Document(collection = "user")
public class User implements UserDetails {

    @Id
    private String id;
    private Location selectedLocation;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Integer phone;
    private String address;
    private String profilePicName;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    @DBRef
    private List<Display> displays = new ArrayList<Display>();

    @DBRef
    private List<Board> boards = new ArrayList<Board>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
