package com.example.test;

import com.example.models.Event;
import com.example.models.User;
import com.example.repository.EventRepository;
import com.example.repository.exception.EventNotFoundException;
import com.example.repository.exception.UserNotFoundException;
import com.example.repository.UserRepository;

import java.util.Scanner;

public class GetEvent {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("event id: ");
        int user_id = sc.nextInt();

        EventRepository repository = new EventRepository();

        try {
            Event event = repository.get(user_id);
            System.out.println(event.toString());
        }  catch (EventNotFoundException e){
            System.out.println("null");
        }

        sc.close();
    }
}
