package spring.ch4_exception.a_dao_interface;

import spring.ch1_object_and_dependency.User;

import java.util.List;

public interface UserDao {

    void add(User user);

    User get(String userId);

    List<User> getAll();

    int getCount();

    void deleteAll();
}
