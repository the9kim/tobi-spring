package spring.ch5_service_abstraction.d_transaction_demarcation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy18 {

    PreparedStatement makePreparedStatement() throws SQLException;
}
