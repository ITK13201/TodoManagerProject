package com.example;

import com.example.utils.Command;
import com.example.utils.Process;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Error: Input Port Number");
            System.exit(1);
        }
        int PORT = Integer.parseInt(args[0]);

        InetAddress addr = InetAddress.getByName("localhost");
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, PORT);
        try {
            System.out.println("socket = " + socket);
            BufferedReader socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socket_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            Scanner sc = new Scanner(System.in);
            Process process = new Process(socket_in, socket_out);
            process.displayLOGO();
            while(true) {
                Command cmd = process.getCommand(sc);

                if(cmd.name().equals("logout")) break;
                switch (cmd.name()) {
                    case "signup":
                        process.signup();
                        break;
                    case "login":
                        break;
                    case "create":
                        break;
                    case "change":
                        break;
                    case "delete":
                        break;
                    case "show":
                        break;
                    default:
                        break;
                }
            }
            sc.close();
        } finally {
            socket.close();
        }
    }
}
