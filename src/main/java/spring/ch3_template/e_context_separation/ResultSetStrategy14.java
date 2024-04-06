package spring.ch3_template.e_context_separation;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetStrategy14 {

    Object makeObject(ResultSet resultSet) throws SQLException;
}
