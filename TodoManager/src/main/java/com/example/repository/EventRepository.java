package com.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.model.Event;
import com.example.model.User;
import com.example.repository.exception.EventNotFoundException;
import com.example.repository.exception.UserNameAlreadyUsedException;
import com.example.repository.exception.UserNotFoundException;

public class EventRepository extends Repository {
    public EventRepository() {
        ERROR_MESSAGE = new HashMap<String, String>();
        ERROR_MESSAGE.put("EVENT_TITLE_NOT_NULL", "The title is a required field.");
        ERROR_MESSAGE.put("EVENT_NOT_FOUND", "Event not found.");
    }

    public int add(Event event) {
        int auto_increment_key = -1;
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);

            PreparedStatement ps = null;
            try {
                ps = db.prepareStatement(
                    "INSERT INTO events (title, description, deadline, user_id) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1, event.getTitle());
                ps.setString(2, event.getDescription());
                ps.setTimestamp(3, event.getDeadline());
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

    public Event get(Event event) throws EventNotFoundException {
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
                    "SELECT * FROM events WHERE id = ? AND user_id = ?"
                );
                ps.setInt(1, event.getId());
                ps.setInt(2, event.getUser().getId());
                res = ps.executeQuery();

                if (res.next()) {
                    event.setId(res.getInt("id"));
                    event.setTitle(res.getString("title"));
                    event.setDescription(res.getString("description"));
                    event.setDeadline(res.getTimestamp("deadline"));
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
            User user = repository.getById(user_id);
            event.setUser(user);
        }  catch (UserNotFoundException e){
            e.printStackTrace();
        }

        return event;
    }

    public List<Event> getAll(User user) {
        UserRepository repository = new UserRepository();

        try {
            user = repository.getById(user.getId());
        }  catch (UserNotFoundException e){
            e.printStackTrace();
        }

        List<Event> events = new ArrayList<>();
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);

            PreparedStatement ps = null;
            ResultSet res = null;
            try {
                ps = db.prepareStatement(
                    "SELECT * FROM events WHERE user_id = ?"
                );
                ps.setInt(1, user.getId());
                res = ps.executeQuery();

                while (res.next()) {
                    Event event = new Event(
                        res.getInt("id"),
                        res.getString("title"),
                        res.getString("description"),
                        res.getTimestamp("deadline"),
                        res.getTimestamp("finished_at"),
                        user,
                        res.getTimestamp("created_at"),
                        res.getTimestamp("updated_at")
                    );
                    events.add(event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (res != null) {
                    res.close();
                }
            }
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

        return events;
    }

    public void update(Event event) {
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);
    
            PreparedStatement ps = null;
            try {
                ps = db.prepareStatement(
                    "UPDATE events SET title = ?, description = ?, deadline = ?, finished_at = ? WHERE id = ? AND user_id = ?"
                );
    
                ps.setString(1, event.getTitle());
                ps.setString(2, event.getDescription());
                ps.setTimestamp(3, event.getDeadline());
                ps.setTimestamp(4, event.getFinished_at());
                ps.setInt(5, event.getId());
                ps.setInt(6, event.getUser().getId());
                ps.executeUpdate();
    
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
    }
    
    public void delete(Event event) {
        Connection db = null;
        try {
            Class.forName(db_driver);
            db = DriverManager.getConnection(db_url, db_user, db_password);
            db.setAutoCommit(false);
    
            PreparedStatement ps = null;
            try {
                ps = db.prepareStatement(
                    "DELETE FROM events WHERE id = ? AND user_id = ?"
                );
    
                ps.setInt(1, event.getId());
                ps.setInt(2, event.getUser().getId());
                ps.executeUpdate();
    
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
    }
    
}
