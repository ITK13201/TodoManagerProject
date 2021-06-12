package com.example.test;

import com.example.models.User;
import com.example.repository.UserRepository;
import com.example.repository.DuplicatedKeyException;

import java.util.Scanner;

public class AddUser {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        User user = new User();
        while (true) {
            String user_name = sc.next();
            String user_password = sc.next();

            user.setName(user_name);
            user.setPassword(user_password);

            UserRepository repository = new UserRepository();
            try {
                repository.add(user);
                break;
            } catch (DuplicatedKeyException err) {
                System.out.println(err.getMessage());
            }
        }
        sc.close();
    }
}
