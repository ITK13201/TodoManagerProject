package com.example.test;

import com.example.model.Event;
import com.example.repository.EventRepository;
import com.example.repository.exception.EventNotFoundException;

import java.util.Scanner;

public class DeleteEvent {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("event id: ");
        int event_id = sc.nextInt();

        Event event = new Event();
        event.setId(event_id);

        EventRepository repository = new EventRepository();

        repository.delete(event);

        sc.close();
    }
}
