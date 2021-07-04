package com.example.test;

import com.example.model.Event;
import com.example.model.User;
import com.example.model.UserEventRelation;
import com.example.repository.EventRepository_relationsample;
import com.example.repository.exception.UserNameAlreadyUsedException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Scanner;

public class AddEvent_relationsample {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Event event = new Event();
        UserEventRelation relation = new UserEventRelation();
        while (true) {
            System.out.print("title: ");
            String event_title = sc.next();
            System.out.print("description: ");
            String event_description = sc.next();
            System.out.print("deadline(yyyy-MM-dd:HH:mm): ");
            String event_deadline = sc.next();

            Timestamp event_deadline_ts = null;
            try {
                event_deadline_ts = Event.ConvertStringToTimestamp(event_deadline, "yyyy-MM-dd:HH:mm");
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                continue;
            }

            event.setTitle(event_title);
            event.setDescription(event_description);
            event.setDeadline(event_deadline_ts);

            User user = new User();
            user.setId(1);
            relation.setUser(user);

            EventRepository_relationsample repository = new EventRepository_relationsample();
            try {
                repository.add(relation);
                break;
            } catch (UserNameAlreadyUsedException e) {
                System.out.println(e.getMessage());
            }
        }
        sc.close();
    }
}
