package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Psvm {
    public static void main(String[] args) {
        String password = "admin";

            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes());
                StringBuilder hexString = new StringBuilder();

                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }

                System.out.println(hexString.toString());

            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error hashing password", e);
            }
        }
    }

