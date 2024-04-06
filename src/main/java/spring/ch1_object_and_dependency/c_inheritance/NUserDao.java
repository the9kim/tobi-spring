package spring.ch1_object_and_dependency.c_inheritance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NUserDao extends UserDao3 {

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/spring", "root", "kdw71007050^");
    }
}
