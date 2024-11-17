package com.myboard.userservice.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class PinService {

    private static final String AES_SECRET_KEY = "MySecretKey12345"; // 16 bytes for AES-128

    // Method to generate a unique 6-character alphanumeric display pin from displayId
    public String generateUniqueDisplayPin(String displayId) throws Exception {
        // Perform SHA-256 hashing on the displayId
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(displayId.getBytes());

        // Encode the hash in Base64
        String encodedHash = Base64.getEncoder().encodeToString(hash);

        // Extract alphanumeric characters and form the display pin
        StringBuilder displayPin = new StringBuilder();
        for (int i = 0; i < encodedHash.length(); i++) {
            char c = encodedHash.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                displayPin.append(c);
                if (displayPin.length() == 6) break; // Stop after getting 6 characters
            }
        }
        return displayPin.toString(); // Return the generated display pin
    }

}
