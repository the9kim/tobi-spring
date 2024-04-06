package spring.ch3_template.h_generics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy17 {

    PreparedStatement makePreparedStatement(Connection conn) throws SQLException;
}
