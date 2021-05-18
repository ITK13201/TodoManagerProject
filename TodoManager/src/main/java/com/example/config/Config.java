package com.example.config;

public class Config {
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DATABASE_URL = String.format(
        "jdbc:%s",
        System.getenv("DATABASE_URL")
    );
    public static final String DATABASE_USER = System.getenv("DATABASE_USER");
    public static final String DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD");
}
