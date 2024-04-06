package spring.ch1_object_and_dependency.d_interface;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {

    Connection makeConnection() throws SQLException;

}
