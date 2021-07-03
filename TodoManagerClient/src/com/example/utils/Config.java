package com.example.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Config {
    private static final String LOGO_path = "./resources/static/LOGO.txt";
    public static final String LOGO = readLOGO();

    private static String readLOGO() {
        String text = null;
        try {
            text =  Files.lines(Paths.get(Config.LOGO_path), StandardCharsets.UTF_8)
                .collect(Collectors.joining(System.getProperty("line.separator")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
