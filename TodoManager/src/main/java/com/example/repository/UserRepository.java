package com.example.repository;

import java.sql.*;
import java.util.HashMap;

import com.example.models.User;

public class UserRepository extends Repository {
    public UserRepository() {
        ERROR_MESSAGE = new HashMap<String, String>();
        ERROR_MESSAGE.put("USER_NAME_ALREADY_USED", "このユーザ名は使用済みです．");
    }

    public void add(User user) throws UserNameAlreadyUsedException {
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);

            PreparedStatement ps = null;
            try {
                ps = db.prepareStatement(
                    "INSERT INTO users (name, password) VALUES (?, ?)"
                );
                user.hashPassword();

                ps.setString(1, user.getName());
                ps.setString(2, user.getPassword());
                ps.executeUpdate();
                db.commit();
            } catch (SQLException e) {
                db.rollback();
                if (e.getErrorCode() == 1062) {
                    throw new UserNameAlreadyUsedException(ERROR_MESSAGE.get("USER_NAME_ALREADY_USED"));
                } else {
                    System.out.printf("Error: %s, Error code: %d\n", e.getMessage(), e.getErrorCode());
                }
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (UserNameAlreadyUsedException e) {
            throw e;
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            System.err.println("Failed to find jdbc driver.");
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("Failed to connect mysql.");
        } finally {
            try {
                if(db != null) {
                    db.close();
                }
            } catch (SQLException e) {}
        }
    }
}
