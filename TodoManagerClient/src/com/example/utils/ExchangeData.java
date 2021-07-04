package com.example.utils;

import com.example.model.Event;
import com.example.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeData {
    private String header;
    private String command;
    private List<String> contents;
    private String message;
    private User user;
    private List<User> users;
    private Event event;
    private List<Event> events;
    private int status;

    private HashMap<Integer, String> statusMessage = new HashMap<Integer, String>() {
        {
            put(200, "OK");
            put(400, "NG");
        }
    };

    public String getStatusMessage() {
        return statusMessage.get(status);
    }

}
