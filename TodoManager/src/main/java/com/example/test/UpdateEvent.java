package com.example.test;

import com.example.model.Event;
import com.example.model.User;
import com.example.repository.EventRepository;
import com.example.repository.exception.UserNameAlreadyUsedException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Scanner;

public class UpdateEvent {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Event event = new Event();
        while (true) {
            System.out.print("id: ");
            int event_id = sc.nextInt();
            System.out.print("title: ");
            String event_title = sc.next();
            System.out.print("description: ");
            String event_description = sc.next();
            System.out.print("deadline(yyyy-MM-dd:HH:mm): ");
            String event_deadline_str = sc.next();
            System.out.print("finished_at(yyyy-MM-dd:HH:mm): ");
            String event_finished_at_str = sc.next();

            Timestamp event_deadline_ts = null;
            try {
                event_deadline_ts = Event.ConvertStringToTimestamp(event_deadline_str, "yyyy-MM-dd:HH:mm");
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                continue;
            }

            Timestamp event_finished_at_ts = null;
            try {
                event_finished_at_ts = Event.ConvertStringToTimestamp(event_finished_at_str, "yyyy-MM-dd:HH:mm");
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                continue;
            }

            event.setId(event_id);
            event.setTitle(event_title);
            event.setDescription(event_description);
            event.setDeadline(event_deadline_ts);
            event.setFinished_at(event_finished_at_ts);

            User user = new User();
            user.setId(1);
            event.setUser(user);

            EventRepository repository = new EventRepository();
            repository.update(event);
            break;
        }
        sc.close();
    }
}
