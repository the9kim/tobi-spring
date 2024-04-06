package spring.ch3_template.d_strategy_pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatementStrategy13GetCount implements StatementStrategy13 {
    @Override
    public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(users.id) AS count FROM users";
        return conn.prepareStatement(sql);
    }
}
