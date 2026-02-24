package com.aha.aha.utility;

import java.security.SecureRandom;


import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String ALPHANUMERIC = UPPERCASE + NUMBERS;
    private static final int PASSWORD_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();
    
    public String generatePassword() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(ALPHANUMERIC.length());
            password.append(ALPHANUMERIC.charAt(index));
        }
        return password.toString();
    }
}
