package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.common.WorkFlow;
import com.myboard.userservice.controller.model.user.*;
import com.myboard.userservice.entity.Base;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkFlow flow;

    //USER
    @PostMapping("/signup")
    public MainResponse<UserSignupResponse> signup(@RequestBody UserSignupRequest signupRequest) throws MBException {
        userService.handleUserSignup(signupRequest);
        return new MainResponse<>(flow);
    }
    @PostMapping("/login")
    public MainResponse<UserLoginResponse> login(@RequestBody UserLoginRequest loginRequest) throws MBException {
        userService.handleUserLogin(loginRequest);
        return new MainResponse<>(flow);
    }

    @GetMapping("/profile-pic")
    public ResponseEntity<byte[]> getLoggedInUserProfilePic() {
        try {
            Resource profilePic = userService.getLoggedInUserProfilePic();

            if (profilePic == null) {
                return ResponseEntity.notFound().build();
            }

            // Read the Resource content into a byte array
            byte[] imageBytes = readResourceToByteArray(profilePic);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + profilePic.getFilename() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private byte[] readResourceToByteArray(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }
    @PostMapping("/profile-pic")
    public String saveLoggedInProfilePic(@RequestParam("file") MultipartFile file) {
        try {
            userService.saveLoggedInUserProfilePic(file);
            return "Profile picture uploaded successfully";
        } catch (MBException | IOException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/update")
    public MainResponse<Void> updateUserDetails(@RequestBody UserDetailsRequest userDetailsRequest) throws MBException {
        userService.updateUserDetails(userDetailsRequest);
        return new MainResponse<>(flow);
    }

    @GetMapping("/location")
    public MainResponse<String> getUserCity() throws MBException {
        String userCityName = userService.getUserCity();
        return buildResponse(userCityName);
    }


}
