package com.example.test;

import java.sql.*;

import static com.example.config.Config.*;
import com.example.models.User;

public class Database {
    public static void main(String[] args) {
        String driver = MYSQL_DRIVER;
        String url = DATABASE_URL;
        String user = DATABASE_USER;
        String password = DATABASE_PASSWORD;

        String msg = "";

        try {
            // ドライバロード
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, password);

            // ステートメント生成
            Statement statement = con.createStatement();

            // SQL を実行
            String sql = "SELECT * FROM users";
            ResultSet result = statement.executeQuery(sql);

            // 結果行をループ
            while(result.next()){
                User user1 = new User(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getString("password"),
                    result.getString("role"),
                    result.getTimestamp("created_at"),
                    result.getTimestamp("updated_at")
                );
                //表示
                System.out.printf("id: %d\n", user1.getId());
                System.out.printf("name: %s\n", user1.getName());
                System.out.printf("password: %s\n", user1.getPassword());
                System.out.printf("role: %s\n", user1.getRole());
                System.out.printf("created_at: %s\n", user1.getCreated_at());
                System.out.printf("updated_at: %s\n", user1.getUpdated_at());
            }

            // 接続を閉じる
            result.close();
            statement.close();
            con.close();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            msg = "ドライバのロードに失敗しました";
            System.out.println(msg);

        }catch (Exception e){
            e.printStackTrace();
            msg = "コネクションに゙に失敗しました";
            System.out.println(msg);
        }
    }
}
