package spring.ch3_template.e_context_separation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy14 {

    PreparedStatement makePreparedStatement(Connection conn) throws SQLException;
}
