package spring.ch1_object_and_dependency.g_application_context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {

    @Override
    public Connection makeConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/spring", "root", "kdw71007050^");
    }
}
