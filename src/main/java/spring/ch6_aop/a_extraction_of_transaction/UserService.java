package spring.ch6_aop.a_extraction_of_transaction;

import spring.ch5_service_abstraction.h_mail_service.User3;

import java.sql.SQLException;

public interface UserService {

    void add(User3 user3);

    void upgradeLevels() throws SQLException;
}
