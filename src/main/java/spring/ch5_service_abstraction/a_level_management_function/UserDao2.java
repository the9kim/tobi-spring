package spring.ch5_service_abstraction.a_level_management_function;

import spring.ch5_service_abstraction.User2;

import java.util.List;

public interface UserDao2 {

    void add(User2 user);

    User2 get(String userId);

    List<User2> getAll();

    int getCount();

    void update(User2 user);

    void deleteAll();
}
