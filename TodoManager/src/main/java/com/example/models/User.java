package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    private String password;
    private String role;
    private Timestamp created_at;
    private Timestamp updated_at;

    public String hashPassword() throws NoSuchAlgorithmException {
        // SHA-256（SHA-2）
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] sha256_result = sha256.digest(password.getBytes());
        System.out.printf("Successfully user: %s password hashed.\n", name);

        password = String.format("%040x", new BigInteger(1, sha256_result));;

        return password;
    }
}
