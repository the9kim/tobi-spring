package spring.ch6_aop.b_unit_test_and_isolation;

import spring.ch5_service_abstraction.h_mail_service.User3;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao3 {

    private List<User3> users;
    private List<User3> updated;

    public MockUserDao(List<User3> users) {
        this.users = users;
        this.updated = new ArrayList<>();
    }

    @Override
    public List<User3> getAll() {
        return this.users;
    }

    @Override
    public void update(User3 user) {
        this.updated.add(user);
    }

    @Override
    public void add(User3 user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User3 get(String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }


    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    public List<User3> getUpdated() {
        return updated;
    }
}
