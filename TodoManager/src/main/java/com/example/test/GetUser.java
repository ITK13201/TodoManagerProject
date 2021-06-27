package com.example.test;

import com.example.models.User;
import com.example.repository.UserNotFoundException;
import com.example.repository.UserRepository;

import java.util.Scanner;

public class GetUser {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("user id: ");
        int user_id = sc.nextInt();

        UserRepository repository = new UserRepository();

        try {
            User user = repository.get(user_id);
            System.out.println(user.toString());
        }  catch (UserNotFoundException e){
            System.out.println("null");
        }

        sc.close();
    }
}
