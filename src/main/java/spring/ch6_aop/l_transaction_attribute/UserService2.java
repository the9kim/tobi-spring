package spring.ch6_aop.l_transaction_attribute;

import spring.ch5_service_abstraction.h_mail_service.User3;

import java.sql.SQLException;
import java.util.List;

public interface UserService2 {

    void add(User3 user3);

    void upgradeLevels() throws SQLException;

    List<User3> getAll();

    int getCount();

    void update(User3 user);

    void deleteAll();
}
