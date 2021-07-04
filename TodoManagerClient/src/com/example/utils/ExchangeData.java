package com.example.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.example.model.Event;
import com.example.model.User;

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

//    EXAMPLE
//        -----------------------------------------------------
//        | ID | TITLE | DESCRIPTION | DEADLINE | IS FINISHED |
//        -----------------------------------------------------
//        | 1  | a     | b           | 2021     | o           |
//        -----------------------------------------------------
    public void printEventsTable() {
        String columns[] = {
            "ID", "TITLE", "DESCRIPTION", "DEADLINE", "IS FINISHED"
        };
        int eventsSize = events.size();
        int lengths[][] = new int[columns.length][eventsSize + 1];
        int maxLengths[] = new int[columns.length];

        for (int i = 0; i < columns.length; i++) {
            lengths[i][0] = columns[i].length();
        }
        for (int i = 0; i < eventsSize; i++) {
            Event eventElem = events.get(i);
            lengths[0][i+1] = Integer.valueOf(eventElem.getId()).toString().length();
            lengths[1][i+1] = eventElem.getTitle().length();
            lengths[2][i+1] = eventElem.getDescription().length();
            lengths[3][i+1] = eventElem.getDeadline().toString().length();
            lengths[4][i+1] = 1;
        }
        for (int i = 0; i < columns.length; i++) {
            maxLengths[i] = Arrays.stream(lengths[i]).max().getAsInt();
        }

        int horizontalLength = Arrays.stream(maxLengths).sum() + 3*(columns.length - 1) + 2*2;

        // Start Print
        System.out.println();
        System.out.println(StringUtils.repeat("-", horizontalLength));
        for (int i = 0; i < columns.length; i++) {
            if (i == 0) {
                System.out.print("| ");
            } else {
                System.out.print(" | ");
            }
            System.out.print(columns[i]);
            System.out.print(StringUtils.repeat(" ", maxLengths[i] - columns[i].length()));
            if (i == columns.length - 1) {
                System.out.println(" |");
            }
        }
        System.out.println(StringUtils.repeat("-", horizontalLength));
        for (int i = 0; i < eventsSize; i++) {
            Event eventElem = events.get(i);
            System.out.print("| ");
            String id_str = Integer.valueOf(eventElem.getId()).toString();
            System.out.print(id_str);
            System.out.print(StringUtils.repeat(" ", maxLengths[0] - id_str.length()));
            System.out.print(" | ");
            System.out.print(eventElem.getTitle());
            System.out.print(StringUtils.repeat(" ", maxLengths[1] - eventElem.getTitle().length()));
            System.out.print(" | ");
            System.out.print(eventElem.getDescription());
            System.out.print(StringUtils.repeat(" ", maxLengths[2] - eventElem.getDescription().length()));
            System.out.print(" | ");
            System.out.print(eventElem.getDeadline().toString());
            System.out.print(StringUtils.repeat(" ", maxLengths[3] - eventElem.getDeadline().toString().length()));
            System.out.print(" | ");
            boolean is_finished = eventElem.getFinished_at() != null;
            if (is_finished) System.out.print("o");
            else System.out.print(" ");
            System.out.print(StringUtils.repeat(" ", maxLengths[4] - 1));
            System.out.println(" |");
        }
        System.out.println(StringUtils.repeat("-", horizontalLength));
        System.out.println();
    }

}
