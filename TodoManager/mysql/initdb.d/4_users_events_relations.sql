DROP TABLE IF EXISTS users_events;

CREATE TABLE users_events_relations (
    id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    event_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_events_relations_user_id
        FOREIGN KEY (user_id) 
        REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_users_events_relations_event_id
        FOREIGN KEY (event_id)
        REFERENCES events (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);
