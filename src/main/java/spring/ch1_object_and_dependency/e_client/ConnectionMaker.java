package spring.ch1_object_and_dependency.e_client;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {

    Connection makeConnection() throws SQLException;

}
