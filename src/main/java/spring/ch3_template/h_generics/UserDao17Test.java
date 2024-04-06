package spring.ch3_template.h_generics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch1_object_and_dependency.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/dao17-applicationContext.xml")
public class UserDao17Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch3_template.h_generics.UserDao17Test");
    }

    @Autowired
    private UserDao17 userDao17;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        user1 = new User("1", "roy", "1234");
        user2 = new User("2", "hoy", "1234");
        user3 = new User("3", "doy", "1234");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        userDao17.deleteAll();
        assertThat(userDao17.getCount(), is(0));

        userDao17.add(user1);
        userDao17.add(user2);
        assertThat(userDao17.getCount(), is(2));

        User savedUser1 = userDao17.get(user1.getId());
        assertThat(savedUser1.getName(), is(user1.getName()));
        assertThat(savedUser1.getPassword(), is(user1.getPassword()));

        User savedUser2 = userDao17.get(user2.getId());
        assertThat(savedUser2.getName(), is(user2.getName()));
        assertThat(savedUser2.getPassword(), is(user2.getPassword()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException{
        userDao17.deleteAll();
        assertThat(userDao17.getCount(), is(0));

        userDao17.get("unknown_id");
    }

    @Test
    public void getCount() throws SQLException {
        userDao17.deleteAll();
        assertThat(userDao17.getCount(), is(0));

        userDao17.add(user1);
        assertThat(userDao17.getCount(), is(1));

        userDao17.add(user2);
        assertThat(userDao17.getCount(), is(2));

        userDao17.add(user3);
        assertThat(userDao17.getCount(), is(3));
    }
}
