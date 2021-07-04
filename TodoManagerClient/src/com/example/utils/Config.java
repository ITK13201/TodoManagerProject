package com.example.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {
    private static final String LOGO_PATH = "./resources/static/LOGO.txt";
    public static final String LOGO = readTextFile(LOGO_PATH);
    public static final String TOKEN_PATH = "./resources/dynamic/TOKEN.txt";

    public static String readTextFile(final String path) {
        String text = null;
        try {
            text =  Files.lines(Paths.get(path), StandardCharsets.UTF_8)
                .collect(Collectors.joining(System.getProperty("line.separator")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static void writeTextFile(final String path, String text) {
        List<String> lines = Stream.of(text).collect(Collectors.toList());;
        try {
            Files.write(Paths.get(path), lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean existsTextFile(final String path) {
        return Files.exists(Paths.get(path));
    }
}
