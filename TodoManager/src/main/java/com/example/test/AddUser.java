package com.example.test;

import com.example.models.User;
import com.example.repository.UserRepository;
import com.example.repository.UserNameAlreadyUsedException;

import java.util.Scanner;

public class AddUser {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        User user = new User();
        while (true) {
            System.out.print("user: ");
            String user_name = sc.next();
            System.out.print("password: ");
            String user_password = sc.next();

            user.setName(user_name);
            user.setPassword(user_password);

            UserRepository repository = new UserRepository();
            try {
                int key = repository.add(user);
                System.out.println(key);
                break;
            } catch (UserNameAlreadyUsedException err) {
                System.out.println(err.getMessage());
            }
        }
        sc.close();
    }
}
