package com.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.example.models.User;

public class UserRepository extends Repository {
    public UserRepository() {
        ERROR_MESSAGE = new HashMap<String, String>();
        ERROR_MESSAGE.put("USER_NAME_ALREADY_USED", "このユーザ名は使用済みです．");
        ERROR_MESSAGE.put("USER_NOT_FOUND", "ユーザが見つかりません．");
    }

    public int add(User user) throws UserNameAlreadyUsedException {
        int auto_increment_key = -1;
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);

            PreparedStatement ps = null;
            try {
                ps = db.prepareStatement(
                    "INSERT INTO users (name, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                user.hashPassword();

                ps.setString(1, user.getName());
                ps.setString(2, user.getPassword());
                ps.executeUpdate();

                ResultSet res = ps.getGeneratedKeys();
                if(res.next()) {
                    auto_increment_key = res.getInt(1);
                    res.close();
                }

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

        return auto_increment_key;
    }

    public User get(int id) throws UserNotFoundException {
        User user = new User();
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);

            PreparedStatement ps = null;
            ResultSet res = null;
            try {
                ps = db.prepareStatement(
                    "SELECT * FROM users WHERE id = ?"
                );
                ps.setInt(1, id);
                res = ps.executeQuery();

                if (res.next()) {
                    user.setId(res.getInt("id"));
                    user.setName(res.getString("name"));
                    user.setPassword(res.getString("password"));
                    user.setRole(res.getString("role"));
                    user.setCreated_at(res.getTimestamp("created_at"));
                    user.setUpdated_at(res.getTimestamp("updated_at"));
                } else {
                    throw new UserNotFoundException(ERROR_MESSAGE.get("USER_NOT_FOUND"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (res != null) {
                    res.close();
                }
            }
        } catch (UserNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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

        return user;
    }
}
