package spring.ch3_template.b_method_extraction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch1_object_and_dependency.User;
import spring.ch3_template.a_exception_handling.UserDao10;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/dao11-applicationContext.xml")
public class UserDao11Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch3_template.b_method_extraction.UserDao11Test");
    }

    @Autowired
    private UserDao11 userDao11;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        user1 = new User("1", "roy", "1234");
        user2 = new User("2", "hoy", "1234");
        user3 = new User("3", "doy", "1234");

        System.out.println(userDao11.getDataSource());
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        userDao11.deleteAll();
        assertThat(userDao11.getCount(), is(0));

        userDao11.add(user1);
        userDao11.add(user2);
        assertThat(userDao11.getCount(), is(2));

        User savedUser1 = userDao11.get(user1.getId());
        assertThat(savedUser1.getName(), is(user1.getName()));
        assertThat(savedUser1.getPassword(), is(user1.getPassword()));

        User savedUser2 = userDao11.get(user2.getId());
        assertThat(savedUser2.getName(), is(user2.getName()));
        assertThat(savedUser2.getPassword(), is(user2.getPassword()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        userDao11.deleteAll();
        assertThat(userDao11.getCount(), is(0));

        userDao11.get("unknown_id");
    }

    @Test
    public void getCount() throws SQLException {
        userDao11.deleteAll();
        assertThat(userDao11.getCount(), is(0));

        userDao11.add(user1);
        assertThat(userDao11.getCount(), is(1));

        userDao11.add(user2);
        assertThat(userDao11.getCount(), is(2));

        userDao11.add(user3);
        assertThat(userDao11.getCount(), is(3));
    }
}
