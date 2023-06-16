package com.myboard.userservice.security;
import java.util.Base64;
import java.security.SecureRandom;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom(); // creates a secure random number generator
        byte[] secretKey = new byte[32]; // creates a byte array
        secureRandom.nextBytes(secretKey); // fills the byte array with random bytes
        String encodedKey = Base64.getEncoder().encodeToString(secretKey); // encodes the bytes in base64 to get a string
        System.out.println("Your secret key is: " + encodedKey);
    }
}
