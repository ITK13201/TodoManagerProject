package com.example.repository;

import com.example.models.Event;
import com.example.models.User;
import com.example.models.UserEventRelation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;

public class EventRepository extends Repository {
    public EventRepository() {
        ERROR_MESSAGE = new HashMap<String, String>();
        ERROR_MESSAGE.put("EVENT_TITLE_NOT_NULL", "タイトルは必須項目です．");
    }

    public void add(Event event, UserEventRelation relation) throws UserNameAlreadyUsedException {
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);

            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            try {
                ps1 = db.prepareStatement(
                    "INSERT INTO events (title, description, begin_at) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps2 = db.prepareStatement(
                    "INSERT INTO users_events (user_id, event_id) VALUES (?, ?)"
                );

                ps1.setString(1, event.getTitle());
                ps1.setString(2, event.getDescription());
                ps1.setTimestamp(3, event.getBegin_at());
                ps1.executeUpdate();

                ResultSet res = ps1.getGeneratedKeys();
                int auto_increment_key;
                if (res.next()) {
                    auto_increment_key = res.getInt(1);

                    ps2.setInt(1, relation.getUser_id());
                    ps2.setInt(2, auto_increment_key);
                    ps2.executeUpdate();

                    res.close();
                }

                db.commit();
            } catch (SQLException e) {
                db.rollback();
                if (e.getErrorCode() == 1048) {
                    throw new UserNameAlreadyUsedException(ERROR_MESSAGE.get("EVENT_TITLE_NOT_NULL"));
                } else {
                    System.out.printf("Error: %s, Error code: %d\n", e.getMessage(), e.getErrorCode());
                }
            } finally {
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
            }
        } catch (UserNameAlreadyUsedException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Failed to find jdbc driver.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to connect mysql.");
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (SQLException e) {
            }
        }
    }
}
