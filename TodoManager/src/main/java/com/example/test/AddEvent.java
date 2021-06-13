package com.example.test;

import com.example.models.Event;
import com.example.repository.EventRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserNameAlreadyUsedException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Scanner;

public class AddEvent {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Event event = new Event();
        while (true) {
            System.out.print("title: ");
            String event_title = sc.next();
            System.out.print("description: ");
            String event_description = sc.next();
            System.out.print("begin_at(yyyy-MM-dd:HH:mm): ");
            String event_begin_at = sc.next();

            Timestamp event_begin_at_ts = null;
            try {
                event_begin_at_ts = Event.ConvertStringToTimestamp(event_begin_at, "yyyy-MM-dd:HH:mm");
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                continue;
            }

            event.setTitle(event_title);
            event.setDescription(event_description);
            event.setBegin_at(event_begin_at_ts);

            EventRepository repository = new EventRepository();
            try {
                repository.add(event);
                break;
            } catch (UserNameAlreadyUsedException e) {
                System.out.println(e.getMessage());
            }
        }
        sc.close();
    }
}
