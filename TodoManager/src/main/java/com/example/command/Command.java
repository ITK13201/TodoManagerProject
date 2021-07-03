package com.example.command;

import java.io.BufferedReader;
import java.io.PrintWriter;


public class Command {
    BufferedReader socket_in;
    PrintWriter socket_out;

    public Command(BufferedReader socket_in, PrintWriter socket_out) {
        this.socket_in = socket_in;
        this.socket_out = socket_out;
    }


}
