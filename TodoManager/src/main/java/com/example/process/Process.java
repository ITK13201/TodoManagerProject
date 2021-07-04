package com.example.process;

import com.example.command.Command;
import com.example.model.User;
import com.example.model.Event;
import com.example.repository.EventRepository;
import com.example.repository.UserRepository;
import com.example.repository.exception.UserNameAlreadyUsedException;
import com.example.repository.exception.UserNotFoundException;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Process {
    private HashMap<String, String> acceptedMessage = new HashMap<String, String>() {
        {
            put("signup", "Successfully signed up!");
            put("login", "Successfully logged in!");
            put("create", "Successfully create event!");
        }
    };

    BufferedReader socket_in;
    PrintWriter socket_out;

    public Process(BufferedReader socket_in, PrintWriter socket_out) {
        this.socket_in = socket_in;
        this.socket_out = socket_out;
    }

    public void signup(ExchangeData receive_data) {
        boolean accepted = false;
        String errMessage = null;
        User user = receive_data.getUser();

        UserRepository repository = new UserRepository();
        try {
            repository.add(user);
            accepted = true;
        } catch (UserNameAlreadyUsedException e) {
            errMessage = e.getMessage();
        }

        Gson gson = new Gson();
        ExchangeData send_data = new ExchangeData();
        send_data.setCommand("signup");
        if (accepted) {
            List<String> contents = new ArrayList<>();
            contents.add(user.getToken());
            send_data.setContents(contents);
            send_data.setStatus(200);
            send_data.setMessage(acceptedMessage.get("signup"));
        } else {
            send_data.setStatus(400);
            send_data.setMessage(errMessage);
        }

        String send_json = gson.toJson(send_data);
        socket_out.println(send_json);
    }

    public void login(ExchangeData receive_data) {
        boolean accepted = false;
        String errMessage = null;
        User user = receive_data.getUser();

        UserRepository repository = new UserRepository();
        try {
            repository.getByToken(user.getToken());
            accepted = true;
        } catch (UserNotFoundException e) {
            errMessage = e.getMessage();
        }

        Gson gson = new Gson();
        ExchangeData send_data = new ExchangeData();
        send_data.setCommand("login");
        if (accepted) {
            send_data.setStatus(200);
            send_data.setMessage(acceptedMessage.get("login"));
        } else {
            send_data.setStatus(400);
            send_data.setMessage(errMessage);
        }

        String send_json = gson.toJson(send_data);
        socket_out.println(send_json);
    }

    public void create(ExchangeData receive_data) {
        boolean accepted = false;
        String errMessage = null;
        Event event = receive_data.getEvent();

        EventRepository eventRepository = new EventRepository();
        UserRepository userRepository = new UserRepository();
        try {
            User user = userRepository.getByToken(receive_data.getHeader());
            event.setUser(user);
        } catch (UserNotFoundException e) {
            errMessage = e.getMessage();
        }

        int res = eventRepository.add(event);
        if (res != 0) {
            accepted = true;
        }

        Gson gson = new Gson();
        ExchangeData send_data = new ExchangeData();
        send_data.setCommand("create");
        if (accepted) {
            send_data.setStatus(200);
            send_data.setMessage(acceptedMessage.get("create"));
        } else {
            send_data.setStatus(400);
            send_data.setMessage(errMessage);
        }

        String send_json = gson.toJson(send_data);
        socket_out.println(send_json);
    }

    public void invalidSyntax() {
        Gson gson = new Gson();
        ExchangeData send_data = new ExchangeData();
        send_data.setStatus(400);
        send_data.setMessage("Error: Invalid Json Syntax!");

        String send_json = gson.toJson(send_data);
        socket_out.println(send_json);
    }
}
