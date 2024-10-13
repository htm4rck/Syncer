package com.ndp.util;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class NDPEncryptPass {
    private static final Logger logger = Logger.getLogger(NDPEncryptPass.class);
    public String getEncryptedPassword(String password, String email) {
        logger.error("password: " + password);
        logger.error("email: " + email);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String reversedEmail = new StringBuilder(email).reverse().toString();
            byte[] hashedEmail = digest.digest(reversedEmail.getBytes(StandardCharsets.UTF_8));

            String hashedPayload = bytesToHex(hashedEmail);
            logger.error("hashedPayload: " + hashedPayload);
            String payload = password + "::" + hashedPayload;
            byte[] hCodeBytes = digest.digest(payload.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(hCodeBytes);
            //return "encryptedPassword";
        } catch (NoSuchAlgorithmException e) {
            logger.error("hashedPayload:Error encrypting password: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
