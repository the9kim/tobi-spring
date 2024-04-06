package spring.ch3_template.d_strategy_pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy13 {

    PreparedStatement makePreparedStatement(Connection conn) throws SQLException;
}
