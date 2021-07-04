package com.example;

import com.example.command.Command;

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

                Command command = new Command(socket_in, socket_out);
                while (true) {
                    String str = socket_in.readLine();
                    if (str.equals("END"))
                        break;
                    System.out.println("Echoing : ");
                    socket_out.println(str);
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

