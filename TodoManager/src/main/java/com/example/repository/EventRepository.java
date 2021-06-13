package com.example.repository;

import com.example.models.Event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class EventRepository extends Repository {
    public EventRepository() {
        ERROR_MESSAGE = new HashMap<String, String>();
        ERROR_MESSAGE.put("EVENT_TITLE_NOT_NULL", "タイトルは必須項目です．");
    }

    public void add(Event event) throws UserNameAlreadyUsedException {
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);

            PreparedStatement ps = null;
            try {
                ps = db.prepareStatement(
                    "INSERT INTO events (title, description, begin_at) VALUES (?, ?, ?)"
                );

                ps.setString(1, event.getTitle());
                ps.setString(2, event.getDescription());
                ps.setTimestamp(3, event.getBegin_at());
                ps.executeUpdate();
                db.commit();
            } catch (SQLException e) {
                db.rollback();
                if (e.getErrorCode() == 1048) {
                    throw new UserNameAlreadyUsedException(ERROR_MESSAGE.get("EVENT_TITLE_NOT_NULL"));
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
