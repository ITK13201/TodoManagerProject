package com.example.test;

import com.example.model.Event;
import com.example.repository.EventRepository;
import com.example.repository.exception.EventNotFoundException;

import java.util.Scanner;

public class GetEvent {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("event id: ");
        int event_id = sc.nextInt();

        EventRepository repository = new EventRepository();

        try {
            Event event = repository.get(event_id);
            System.out.println(event.toString());
        }  catch (EventNotFoundException e){
            System.out.println("null");
        }

        sc.close();
    }
}
