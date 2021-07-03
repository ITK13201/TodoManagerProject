package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
