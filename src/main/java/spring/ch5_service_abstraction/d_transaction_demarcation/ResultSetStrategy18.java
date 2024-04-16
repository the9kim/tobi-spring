package spring.ch5_service_abstraction.d_transaction_demarcation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ResultSetStrategy18<T> {

    T makeObject(ResultSet resultSet) throws SQLException;
}
