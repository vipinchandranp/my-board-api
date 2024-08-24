package com.myboard.userservice.service;

import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MBUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by username in your User repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Return a new instance of the UserDetails object populated with your user data
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() // If you have roles or authorities, populate them here
        );
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the principal is an instance of UserDetails
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User springUser =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            // Fetch the actual User entity from the database using the username
            return userRepository.findByUsername(springUser.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
        // If principal is not an instance of UserDetails, handle accordingly (maybe anonymous user)
        throw new IllegalStateException("User not authenticated");
    }

}
