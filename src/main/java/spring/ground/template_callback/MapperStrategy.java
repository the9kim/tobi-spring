package spring.ground.template_callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MapperStrategy<T> {

    T mapRow(ResultSet rs) throws SQLException;
}
