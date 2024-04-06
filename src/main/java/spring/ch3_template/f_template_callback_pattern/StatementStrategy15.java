package spring.ch3_template.f_template_callback_pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy15 {

    PreparedStatement makePreparedStatement(Connection conn) throws SQLException;
}
