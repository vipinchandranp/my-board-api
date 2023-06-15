package com.myboard.userservice.service;
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Implement user-related business logic methods
    // ...
}

