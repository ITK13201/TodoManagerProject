package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    int id;
    String title;
    String description;
    Timestamp begin_at;
    Timestamp finished_at;
    User user;
    Timestamp created_at;
    Timestamp updated_at;

    public static Timestamp ConvertStringToTimestamp(String datetime_string, String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date parsedDate = format.parse(datetime_string);
        Timestamp datetime_timestamp = new Timestamp(parsedDate.getTime());

        return datetime_timestamp;
    }
}
