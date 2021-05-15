DROP TABLE IF EXISTS users_todo_lists_relationships;

CREATE TABLE users_todo_lists_relationships (
    id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    todo_list_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id) 
        REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_todo_list_id
        FOREIGN KEY (todo_list_id) 
        REFERENCES todo_lists (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);
