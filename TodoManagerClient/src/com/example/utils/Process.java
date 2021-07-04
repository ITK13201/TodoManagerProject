package com.example.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Scanner;

import com.example.model.User;
import com.example.utils.Config;

public class Process {
    BufferedReader socket_in;
    PrintWriter socket_out;

    public Process(BufferedReader socket_in, PrintWriter socket_out) {
        this.socket_in = socket_in;
        this.socket_out = socket_out;
    }

    public void displayLOGO() {
        System.out.println(Config.LOGO);
    }

    public Command getCommand(Scanner sc, final String TOKEN) {
        Command cmd = null;

        boolean accepted = false;
        while(!accepted) {
            System.out.print("> ");
            String input = sc.next();
            try {
                cmd = Command.valueOf(input);

                switch (cmd.name()) {
                    case "signup":
                        if(TOKEN != null) {
                            System.out.println("You're already logged in.");
                        }
                        accepted = true;
                        break;
                    case "login":
                        if(TOKEN != null) {
                            System.out.println("You're already logged in.");
                        } else {
                            boolean is_exists = Config.existsTextFile(Config.TOKEN_PATH);
                            if (is_exists) {
                                accepted = true;
                            } else {
                                System.out.println("Please signup.");
                            }
                        }
                        break;
                    case "exit":
                        accepted = true;
                        break;
                    default:
                        if(TOKEN == null) {
                            System.out.println("This command requires you to be logged in.");
                        }
                        accepted = true;
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid command!");
            }
        }
        return cmd;
    }

    public String signup(Scanner sc) {
        Gson gson = new Gson();
        String token = null;
        ExchangeData send_data = new ExchangeData();
        ExchangeData receive_data = null;
        send_data.setCommand("signup");

        while(true) {
            User user = new User();
            System.out.print("Input username: ");
            String username = sc.next();
            user.setName(username);
            System.out.print("Input password: ");
            String password = sc.next();
            user.setPassword(password);
            send_data.setUser(user);
            String send_json = gson.toJson(send_data);
            socket_out.println(send_json);

            try {
                String receive_json = socket_in.readLine();
                System.out.println("receive: " + receive_json);
                receive_data = gson.fromJson(receive_json, ExchangeData.class);
                String statusMessage = receive_data.getStatusMessage();
                if (statusMessage == null) {
                    System.out.println("Sorry. Failed to sign up. Retype user name.");
                } else if (statusMessage.equals("OK")) {
                    System.out.println(receive_data.getMessage());
                    break;
                } else {
                    System.out.println(receive_data.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        token = receive_data.getContents().get(0);
        Config.writeTextFile(token, Config.TOKEN_PATH);
        return token;
    }

    public String login() {
        Gson gson = new Gson();
        String token = null;
        ExchangeData send_data = new ExchangeData();
        ExchangeData receive_data = null;
        send_data.setCommand("login");

        token = Config.readTextFile(Config.TOKEN_PATH);
        User user = new User();
        user.setToken(token);
        send_data.setUser(user);
        String send_json = gson.toJson(send_data);
        socket_out.println(send_json);

        try {
            String receive_json = socket_in.readLine();
            System.out.println("receive: " + receive_json);
            receive_data = gson.fromJson(receive_json, ExchangeData.class);
            String message = receive_data.getStatusMessage();
            if (message == null) {
                System.out.println("Sorry. Failed to log in.");
                token = null;
            } else if (message.equals("OK")) {
                System.out.println(message);
            } else {
                System.out.println(message);
                token = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }

    public String logout() {
        return null;
    }
}
