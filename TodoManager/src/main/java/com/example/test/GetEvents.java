package com.example.test;

import com.example.model.Event;
import com.example.model.User;
import com.example.repository.EventRepository;

import java.util.List;
import java.util.Scanner;

public class GetEvents {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("user id: ");
        int user_id = sc.nextInt();

        User user = new User();
        user.setId(user_id);
        List<Event> events;
        EventRepository repository = new EventRepository();

        events = repository.getAll(user);
        for (Event event: events) {
            System.out.println(event.toString());
        }

        sc.close();
    }
}
