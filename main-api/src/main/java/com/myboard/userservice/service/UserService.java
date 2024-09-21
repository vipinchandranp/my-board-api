package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.user.UserLoginRequest;
import com.myboard.userservice.controller.model.user.UserLoginResponse;
import com.myboard.userservice.controller.model.user.UserSignupRequest;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.security.JwtUtil;
import com.myboard.userservice.security.MBAuthManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Service
public class UserService {

    @Autowired
    private WorkFlow flow;

    @Autowired
    private MBAuthManager myBoardAuthManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MBUserDetailsService mbUserDetailsService;

    @Value("${myboard.user.profile-pic-path}")
    private String profilePicPath;


    public void handleUserSignup(UserSignupRequest signupRequest) throws MBException {
        // Check if the user already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            flow.addError("User already exists");
            throw new MBException();
        }

        // Create and save the new user
        User newUser = new User();
        newUser.setUsername(signupRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        newUser.setEmail(signupRequest.getEmail());
        newUser.setFirstName(signupRequest.getFirstName());
        newUser.setLastName(signupRequest.getLastName());
        newUser.setPhone(signupRequest.getPhone());

        userRepository.save(newUser);

        // Prepare the response
        String signUpSuccessMessage = messageSource.getMessage("user.register.success", null, Locale.getDefault());
        flow.addInfo(signUpSuccessMessage);
        flow.setData(null);
    }

    public void handleUserLogin(UserLoginRequest loginRequest) throws MBException {
        Authentication authentication = myBoardAuthManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String jwtToken = jwtUtil.generateToken(authentication.getName());

        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setJwtToken(jwtToken);
        flow.setData(loginResponse);
    }


    public Resource getLoggedInUserProfilePic() throws MBException {
        // Retrieve the logged-in user
        User user = mbUserDetailsService.getLoggedInUser();
        String picName = user.getProfilePicName();

        // If the user has no profile picture, return null
        if (picName == null) {
            return null;
        }

        // Construct the file path for the user's profile picture
        Path filePath = Paths.get(profilePicPath).resolve(user.getUsername()).resolve(picName);

        try {
            // Create a Resource object for the file
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the file exists and is readable
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new MBException("Profile picture not found or not readable");
            }
        } catch (MalformedURLException e) {
            throw new MBException("Error while loading profile picture", e);
        }
    }
    public void saveLoggedInUserProfilePic(MultipartFile file) throws MBException, IOException {
        // Retrieve the logged-in user
        User user = mbUserDetailsService.getLoggedInUser();

        // Check if the file is empty
        if (file.isEmpty()) {
            throw new MBException("File is empty");
        }

        // Create a unique file name for the profile picture
        String userName = user.getUsername();
        String fileName = userName + "_" + file.getOriginalFilename();

        // Create the directory path for the user if it doesn't exist
        File userDirectory = new File(profilePicPath + File.separator + userName);
        if (!userDirectory.exists()) {
            userDirectory.mkdirs(); // Create the directory and any necessary parent directories
        }

        // Create the destination file path
        File destinationFile = new File(userDirectory, fileName);

        // Transfer the uploaded file to the destination file
        file.transferTo(destinationFile);

        // Update the user's profile picture name in the database
        user.setProfilePicName(fileName);
        userRepository.save(user);
    }



}
