package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    int id;
    String title;
    String description;
    Timestamp begin_at;
    Timestamp finished_at;
    Timestamp created_at;
    Timestamp updated_at;
}
