package spring.ch3_template.d_strategy_pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatementStrategy13Add implements StatementStrategy13 {
    @Override
    public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
        String sql = "INSERT INTO users(id, name, password) VALUES(?, ?, ?)";
        return conn.prepareStatement(sql);
    }
}
