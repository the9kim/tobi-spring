package spring.ch2_test.c_testing_principles;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import spring.ch1_object_and_dependency.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDao9Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch2_test.c_testing_principles.UserDao9Test");
    }

    private UserDao9 userDao9;
    private User user1;
    private User user2;
    private User user3;

    /**
     * 4. Refactoring - separating method and make fixtures
     */
    @Before
    public void setUp() {
        ApplicationContext context = new GenericXmlApplicationContext("dao9-applicationContext.xml");
        userDao9 = context.getBean("userDao9", UserDao9.class);
        user1 = new User("1", "roy", "1234");
        user2 = new User("2", "hoy", "1234");
        user3 = new User("3", "doy", "1234");

        // Each time test methods are executed, a new application context is created.
        System.out.println(context);
        System.out.println(this);
    }

    /**
     * 1. Testing Consistency
     *
     * To ensure consistent results regardless of the sequence of test execution, it is necessary to position the 'deleteAll()' method at the beginning.
     */
    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        userDao9.deleteAll();
        assertThat(userDao9.getCount(), is(0));

        userDao9.add(user1);
        userDao9.add(user2);
        assertThat(userDao9.getCount(), is(2));

        User savedUser1 = userDao9.get(user1.getId());
        assertThat(savedUser1.getName(), is(user1.getName()));
        assertThat(savedUser1.getPassword(), is(user1.getPassword()));

        User savedUser2 = userDao9.get(user2.getId());
        assertThat(savedUser2.getName(), is(user2.getName()));
        assertThat(savedUser2.getPassword(), is(user2.getPassword()));
    }


    /**
     * 2. Comprehensive Test - Negative test
     * 3. TDD
     */
    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        userDao9.deleteAll();
        assertThat(userDao9.getCount(), is(0));

        userDao9.get("unknown_id");
    }

    @Test
    public void getCount() throws SQLException {
        userDao9.deleteAll();
        assertThat(userDao9.getCount(), is(0));

        userDao9.add(user1);
        assertThat(userDao9.getCount(), is(1));

        userDao9.add(user2);
        assertThat(userDao9.getCount(), is(2));

        userDao9.add(user3);
        assertThat(userDao9.getCount(), is(3));
    }
}
