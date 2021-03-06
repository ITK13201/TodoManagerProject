package com.example.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Config {
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DATABASE_URL = String.format(
        "jdbc:%s",
        System.getenv("DATABASE_URL")
    );
    public static final String DATABASE_USER = System.getenv("DATABASE_USER");
    public static final String DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD");
}
