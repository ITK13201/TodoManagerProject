package com.example.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Scanner;

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

    public Command getCommand(Scanner sc) {
        Command cmd = null;

        while(true) {
            System.out.print("> ");
            String input = sc.next();
            try {
                cmd = Command.valueOf(input);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid command!");
            }
        }
        return cmd;
    }

    public void signup() {
        ExchangeData send_data = new ExchangeData(
            "",
            "signup",
            Collections.emptyList()
        );
        Gson gson = new Gson();
        String send_json = gson.toJson(send_data);
        socket_out.println(send_json);

        try {
            String receive_json = socket_in.readLine();
            System.out.println("receive: " + receive_json);
            ExchangeData receive_data = gson.fromJson(receive_json, ExchangeData.class);
            // TODO
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(String token) {
        ExchangeData data = new ExchangeData(
            token,
            "logout",
            Collections.emptyList()
        );

        Gson gson = new Gson();
        String json = gson.toJson(data);

        System.out.println(json);
        socket_out.println(json);
    }
}
