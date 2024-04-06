package spring.ch3_template.g_embedded_callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy16 {

    PreparedStatement makePreparedStatement(Connection conn) throws SQLException;
}
