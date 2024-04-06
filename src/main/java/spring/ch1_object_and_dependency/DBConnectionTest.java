package spring.ch1_object_and_dependency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] arg) {

        String url = "jdbc:mysql://localhost:3306/spring";
        String username = "root";
        String password = "kdw71007050^";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
