package spring.ch5_service_abstraction.h_mail_service;

import spring.ch5_service_abstraction.User2;

import java.util.List;

public interface UserDao3 {

    void add(User3 user);

    User3 get(String userId);

    List<User3> getAll();

    int getCount();

    void update(User3 user);

    void deleteAll();
}
