package com.example.models;

import java.sql.Timestamp;

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

    public User(){}
    public User(int id, String name, String password, String public_key, String secret_key, String token, String role, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.public_key = public_key;
        this.secret_key = secret_key;
        this.token = token;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPublic_key() {
        return public_key;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }
}
