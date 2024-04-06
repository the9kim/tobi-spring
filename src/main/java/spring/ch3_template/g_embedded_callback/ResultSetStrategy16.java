package spring.ch3_template.g_embedded_callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetStrategy16 {

    Object makeObject(ResultSet resultSet) throws SQLException;
}
