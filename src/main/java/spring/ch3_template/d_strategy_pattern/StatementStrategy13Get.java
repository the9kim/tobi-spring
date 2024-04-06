package spring.ch3_template.d_strategy_pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatementStrategy13Get implements StatementStrategy13 {
    @Override
    public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        return conn.prepareStatement(sql);
    }
}
