package spring.ch3_template.c_template_method_pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao12Add extends UserDao12 {
    @Override
    protected PreparedStatement makeStatement(Connection conn) throws SQLException {
        String sql = "INSERT INTO users(id, name, password) VALUES(?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps;
    }
}
