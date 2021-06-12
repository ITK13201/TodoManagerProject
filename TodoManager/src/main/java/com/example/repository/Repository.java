package com.example.repository;

import java.util.HashMap;

import static com.example.config.Config.*;

public class Repository {
    static String db_driver = MYSQL_DRIVER;
    static String db_url = DATABASE_URL;
    static String db_user = DATABASE_USER;
    static String db_password = DATABASE_PASSWORD;

    static HashMap<String, String> ERROR_MESSAGE;
}
