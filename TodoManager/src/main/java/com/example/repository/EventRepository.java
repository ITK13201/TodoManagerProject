package com.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.example.models.Event;
import com.example.models.User;
import com.example.repository.exception.EventNotFoundException;
import com.example.repository.exception.UserNameAlreadyUsedException;
import com.example.repository.exception.UserNotFoundException;

public class EventRepository extends Repository {
    public EventRepository() {
        ERROR_MESSAGE = new HashMap<String, String>();
        ERROR_MESSAGE.put("EVENT_TITLE_NOT_NULL", "タイトルは必須項目です．");
        ERROR_MESSAGE.put("EVENT_NOT_FOUND", "イベントが見つかりません．");
    }

    public int add(Event event) throws UserNameAlreadyUsedException {
        int auto_increment_key = -1;
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);

            PreparedStatement ps = null;
            try {
                ps = db.prepareStatement(
                    "INSERT INTO events (title, description, begin_at, user_id) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, event.getTitle());
                ps.setString(2, event.getDescription());
                ps.setTimestamp(3, event.getBegin_at());
                ps.setInt(4, event.getUser().getId());
                ps.executeUpdate();

                ResultSet res = ps.getGeneratedKeys();
                if (res.next()) {
                    auto_increment_key = res.getInt(1);
                    res.close();
                }

                db.commit();
            } catch (SQLException e) {
                db.rollback();
                System.out.printf("Error: %s, Error code: %d\n", e.getMessage(), e.getErrorCode());
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
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

        return auto_increment_key;
    }

    public Event get(int id) throws EventNotFoundException {
        Event event = new Event();
        int user_id = -1;
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);

            PreparedStatement ps = null;
            ResultSet res = null;
            try {
                ps = db.prepareStatement(
                    "SELECT * FROM events WHERE id = ?"
                );
                ps.setInt(1, id);
                res = ps.executeQuery();

                if (res.next()) {
                    event.setId(res.getInt("id"));
                    event.setTitle(res.getString("title"));
                    event.setDescription(res.getString("description"));
                    event.setBegin_at(res.getTimestamp("begin_at"));
                    event.setFinished_at(res.getTimestamp("finished_at"));
                    user_id = res.getInt("user_id");
                    event.setCreated_at(res.getTimestamp("created_at"));
                    event.setUpdated_at(res.getTimestamp("updated_at"));
                } else {
                    throw new EventNotFoundException(ERROR_MESSAGE.get("EVENT_NOT_FOUND"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (res != null) {
                    res.close();
                }
            }
        } catch (EventNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
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

        UserRepository repository = new UserRepository();
        try {
            User user = repository.get(user_id);
            event.setUser(user);
        }  catch (UserNotFoundException e){
            e.printStackTrace();
        }

        return event;
    }
}
