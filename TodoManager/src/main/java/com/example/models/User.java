package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    private String password;
    private String public_key;
    private String secret_key;
    private String token;
    private String role;
    private Timestamp created_at;
    private Timestamp updated_at;
}
