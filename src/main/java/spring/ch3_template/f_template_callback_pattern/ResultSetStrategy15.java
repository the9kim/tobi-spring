package spring.ch3_template.f_template_callback_pattern;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetStrategy15 {

    Object makeObject(ResultSet resultSet) throws SQLException;
}
