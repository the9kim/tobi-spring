package spring.ch3_template.h_generics;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetStrategy17<T> {

    T makeObject(ResultSet resultSet) throws SQLException;
}
