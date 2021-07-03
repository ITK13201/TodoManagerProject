package com.example.test;

import com.example.model.User;
import com.example.repository.exception.UserNotFoundException;
import com.example.repository.UserRepository;

import java.util.Scanner;

public class GetUser {
    static void getById() {
        Scanner sc = new Scanner(System.in);

        System.out.print("user id: ");
        int user_id = sc.nextInt();

        UserRepository repository = new UserRepository();

        try {
            User user = repository.getById(user_id);
            System.out.println(user.toString());
        }  catch (UserNotFoundException e){
            System.out.println("null");
        }

        sc.close();
    }

    static void getByName() {
        Scanner sc = new Scanner(System.in);

        System.out.print("user name: ");
        String username = sc.next();

        UserRepository repository = new UserRepository();

        try {
            User user = repository.getByName(username);
            System.out.println(user.toString());
        }  catch (UserNotFoundException e){
            System.out.println("null");
        }

        sc.close();
    }

    static void getByToken() {
        Scanner sc = new Scanner(System.in);

        System.out.print("user id: ");
        int user_id = sc.nextInt();

        UserRepository repository = new UserRepository();

        User user = null;
        try {
            user = repository.getById(user_id);
        }  catch (UserNotFoundException e){
            System.out.println("null");
        }

        sc.close();

        try {
            user = repository.getByToken(user.getToken());
            System.out.println(user.toString());
        }  catch (UserNotFoundException e){
            System.out.println("null");
        }
    }

    public static void main(String[] args) {
        getByToken();
    }
}
