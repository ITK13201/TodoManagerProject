package com.example;

import com.example.process.Dataformat;
import com.example.process.ExchangeData;
import com.google.gson.JsonSyntaxException;

import com.example.command.Command;
import com.example.process.Process;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Error: Input Port Number");
            System.exit(1);
        }
        int PORT = Integer.parseInt(args[0]);

        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Started: " + s);
        while(true) {
            Socket socket = s.accept();
            try {
                System.out.println("Connection accepted: " + socket);
                BufferedReader socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter socket_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Process process = new Process(socket_in, socket_out);
                while (true) {
                    String receive_json = socket_in.readLine();
                    if (receive_json.equals("END")) break;
                    try {
                        ExchangeData receive_data = Dataformat.format(receive_json);
                        Command cmd = Command.valueOf(receive_data.getCommand());
                        if (cmd.name().equals("exit")) break;
                        switch (cmd.name()) {
                            case "signup":
                                process.signup(receive_data);
                                break;
                            case "login":
                                process.login(receive_data);
                                break;
                            case "create":
                                process.create(receive_data);
                                break;
                            case "preChange":
                                process.preChange(receive_data);
                                break;
                            case "change":
                                process.change(receive_data);
                                break;
                            case "delete":
                                process.delete(receive_data);
                                break;
                            case "show":
                                process.show(receive_data);
                                break;
                            default:
                                break;
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        process.invalidSyntax();
                    }
                    System.out.print("receive : ");
                    System.out.println(receive_json);
                }
            } catch (Exception e) {
                System.out.println("Socket Error!");
            } finally {
                System.out.println("closing...");
                socket.close();
            }
        }
    }
}

