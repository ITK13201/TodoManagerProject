package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.models.User;

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
    User user;

    public static Timestamp ConvertStringToTimestamp(String datetime_string, String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date parsedDate = format.parse(datetime_string);
        Timestamp datetime_timestamp = new Timestamp(parsedDate.getTime());

        return datetime_timestamp;
    }
}
