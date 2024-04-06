package spring.ch3_template.c_template_method_pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao12Get extends UserDao12 {
    @Override
    protected PreparedStatement makeStatement(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps;
    }
}
