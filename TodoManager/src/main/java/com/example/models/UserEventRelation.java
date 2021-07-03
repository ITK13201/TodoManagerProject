package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import com.example.models.Event;
import com.example.models.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventRelation {
    private int id;
    private User user;
    private Event event;
    private Timestamp created_at;
    private Timestamp updated_at;
}
