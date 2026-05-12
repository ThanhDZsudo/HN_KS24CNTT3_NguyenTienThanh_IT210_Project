package com.example.healthcare.util;

import java.security.MessageDigest;
import java.util.Base64;

public class HashUtil {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) { return null; }
    }
}