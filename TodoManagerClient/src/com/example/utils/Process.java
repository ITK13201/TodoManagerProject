package com.example.utils;

import com.example.model.Event;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Collections;
import java.util.Scanner;
import java.time.LocalDateTime;

import com.example.model.User;
import com.example.utils.Config;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.ObjectUtils;

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
                        } else {
                            accepted = true;
                        }
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
                        } else {
                            accepted = true;
                        }
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
            receive_data = gson.fromJson(receive_json, ExchangeData.class);
            String statusMessage = receive_data.getStatusMessage();
            if (statusMessage == null) {
                System.out.println("Sorry. Failed to log in.");
                token = null;
            } else if (statusMessage.equals("OK")) {
                System.out.println(receive_data.getMessage());
            } else {
                System.out.println(receive_data.getMessage());
                token = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }

    public String logout() {
        System.out.println("Logged out.");
        return null;
    }

    public void create(Scanner sc, final String TOKEN) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        ExchangeData send_data = new ExchangeData();
        ExchangeData receive_data = null;
        boolean is_valid = false;
        send_data.setCommand("create");
        send_data.setHeader(TOKEN);

        // Eliminate line feeds
        sc.nextLine();

        Event event = new Event();
        String title = "";
        while (title.equals("")){
            System.out.print("Input event title: ");
            title = sc.nextLine();
        }
        event.setTitle(title);

        String description = "";
        while (description.equals("")) {
            System.out.print("Input description: ");
            description = sc.nextLine();
        }
        event.setDescription(description);

        while(!is_valid) {
            System.out.print("Input deadline (yyyy-MM-dd:HH:mm): ");
            String deadline_str = sc.next();
            try {
                Timestamp deadline_ts = Event.ConvertStringToTimestamp(deadline_str, "yyyy-MM-dd:HH:mm");
                event.setDeadline(deadline_ts);
                is_valid = true;
            } catch (ParseException e) {
                System.out.println("Invalid Syntax!");
            }
        }

        send_data.setEvent(event);
        String send_json = gson.toJson(send_data);

        socket_out.println(send_json);

        try {
            String receive_json = socket_in.readLine();
            receive_data = gson.fromJson(receive_json, ExchangeData.class);
            String statusMessage = receive_data.getStatusMessage();
            if (statusMessage == null) {
                System.out.println("Sorry. Failed to create event.");
            } else if (statusMessage.equals("OK")) {
                System.out.println(receive_data.getMessage());
            } else {
                System.out.println(receive_data.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(final String TOKEN) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        ExchangeData send_data = new ExchangeData();
        ExchangeData receive_data = null;
        send_data.setCommand("show");
        send_data.setHeader(TOKEN);

        String send_json = gson.toJson(send_data);
        socket_out.println(send_json);

        try {
            String receive_json = socket_in.readLine();
            receive_data = gson.fromJson(receive_json, ExchangeData.class);
            String statusMessage = receive_data.getStatusMessage();
            if (statusMessage == null) {
                System.out.println("Sorry. Failed to show events.");
            } else if (statusMessage.equals("OK")) {
                System.out.println(receive_data.getMessage());
                receive_data.printEventsTable();
            } else {
                System.out.println(receive_data.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Event preChange(int id, final String TOKEN) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        ExchangeData send_data = new ExchangeData();
        ExchangeData receive_data = null;
        send_data.setCommand("preChange");
        send_data.setHeader(TOKEN);

        Event event = new Event();
        event.setId(id);
        send_data.setEvent(event);

        String send_json = gson.toJson(send_data);
        socket_out.println(send_json);

        try {
            String receive_json = socket_in.readLine();
            receive_data = gson.fromJson(receive_json, ExchangeData.class);
            String statusMessage = receive_data.getStatusMessage();
            if (statusMessage == null) {
                System.out.println("Sorry. Failed to show events.");
            } else if (statusMessage.equals("OK")) {
                System.out.println(receive_data.getMessage());
            } else {
                System.out.println(receive_data.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receive_data.getEvent();
    }

    public void change(Scanner sc, final String TOKEN) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        ExchangeData send_data = new ExchangeData();
        ExchangeData receive_data = null;
        boolean is_valid = false;
        send_data.setCommand("change");
        send_data.setHeader(TOKEN);

        Event event = new Event();
        System.out.print("Input event id: ");
        int id = sc.nextInt();
        event.setId(id);

        Event oldEvent = preChange(id, TOKEN);
        if(oldEvent == null) {
            System.out.println("Sorry. Failed to change event.");
            return;
        }

        // Eliminate line feeds
        sc.nextLine();

        String receive_title;
        if(oldEvent.getTitle() == null) {
            receive_title = "";
        } else {
            receive_title = oldEvent.getTitle();
        }
        System.out.printf("Input event title [%s]: ", receive_title);
        String title = sc.nextLine();
        if(title.equals("")) {
            event.setTitle(oldEvent.getTitle());
        } else {
            event.setTitle(title);
        }

        String receive_description;
        if(oldEvent.getDescription() == null) {
            receive_description = "";
        } else {
            receive_description = oldEvent.getDescription();
        }
        System.out.printf("Input description [%s]: ", receive_description);
        String description = sc.nextLine();
        if(description.equals("")) {
            event.setDescription(oldEvent.getTitle());
        } else {
            event.setDescription(description);
        }

        String receive_deadline;
        if (oldEvent.getDeadline() == null) {
            receive_deadline = "";
        } else {
            receive_deadline = oldEvent.getDeadline().toString();
        }
        while(!is_valid) {
            System.out.printf("Input deadline (yyyy-MM-dd:HH:mm) [%s]: ", receive_deadline);
            String deadline_str = sc.nextLine();

            if(deadline_str.equals("")) {
                event.setDeadline(oldEvent.getDeadline());
                break;
            }

            try {
                Timestamp deadline_ts = Event.ConvertStringToTimestamp(deadline_str, "yyyy-MM-dd:HH:mm");
                event.setDeadline(deadline_ts);
                is_valid = true;
            } catch (ParseException e) {
                System.out.println("Invalid Syntax!");
            }
        }

        String is_finished_str = "";
        while (!(is_finished_str.equals("y") || is_finished_str.equals("n"))) {
            System.out.print("Is finished? [y/n]: ");
            is_finished_str = sc.next();
        }

        Boolean is_finished = false;
        if(is_finished_str.equals("y")) {
            is_finished = true;
        }

        Timestamp finished_at = null;
        if(is_finished) {
            finished_at = Timestamp.valueOf(LocalDateTime.now());
            event.setFinished_at(finished_at);
        }

        send_data.setEvent(event);
        String send_json = gson.toJson(send_data);

        socket_out.println(send_json);

        try {
            String receive_json = socket_in.readLine();
            receive_data = gson.fromJson(receive_json, ExchangeData.class);
            String statusMessage = receive_data.getStatusMessage();
            if (statusMessage == null) {
                System.out.println("Sorry. Failed to change event.");
            } else if (statusMessage.equals("OK")) {
                System.out.println(receive_data.getMessage());
            } else {
                System.out.println(receive_data.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(Scanner sc, final String TOKEN) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        ExchangeData send_data = new ExchangeData();
        ExchangeData receive_data = null;
        send_data.setCommand("delete");
        send_data.setHeader(TOKEN);

        Event event = new Event();
        System.out.print("Input event id: ");
        int id = sc.nextInt();
        event.setId(id);
        send_data.setEvent(event);

        String send_json = gson.toJson(send_data);
        socket_out.println(send_json);

        try {
            String receive_json = socket_in.readLine();
            receive_data = gson.fromJson(receive_json, ExchangeData.class);
            String statusMessage = receive_data.getStatusMessage();
            if (statusMessage == null) {
                System.out.println("Sorry. Failed to delete events.");
            } else if (statusMessage.equals("OK")) {
                System.out.println(receive_data.getMessage());
            } else {
                System.out.println(receive_data.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
