package spring.ch3_template.i_jdbc_template;

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
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/dao18-applicationContext.xml")
public class UserDao18Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch3_template.i_jdbc_template.UserDao18Test");
    }

    @Autowired
    private UserDao18 userDao18;

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
        userDao18.deleteAll();
        assertThat(userDao18.getCount(), is(0));

        userDao18.add(user1);
        userDao18.add(user2);
        assertThat(userDao18.getCount(), is(2));

        User savedUser1 = userDao18.get(user1.getId());
        assertThat(savedUser1.getName(), is(user1.getName()));
        assertThat(savedUser1.getPassword(), is(user1.getPassword()));

        User savedUser2 = userDao18.get(user2.getId());
        assertThat(savedUser2.getName(), is(user2.getName()));
        assertThat(savedUser2.getPassword(), is(user2.getPassword()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException{
        userDao18.deleteAll();
        assertThat(userDao18.getCount(), is(0));

        userDao18.get("unknown_id");
    }

    @Test
    public void getCount() throws SQLException {
        userDao18.deleteAll();
        assertThat(userDao18.getCount(), is(0));

        userDao18.add(user1);
        assertThat(userDao18.getCount(), is(1));

        userDao18.add(user2);
        assertThat(userDao18.getCount(), is(2));

        userDao18.add(user3);
        assertThat(userDao18.getCount(), is(3));
    }

    @Test
    public void getAll() throws SQLException{
        userDao18.deleteAll();

        userDao18.add(user1);
        List<User> users1 = userDao18.getAll();
        checkSameUser(users1.get(0), user1);
        assertThat(users1.size(), is(1));

        userDao18.add(user2);
        List<User> users2 = userDao18.getAll();
        checkSameUser(users2.get(0), user1);
        checkSameUser(users2.get(1), user2);
        assertThat(users2.size(), is(2));

        userDao18.add(user3);
        List<User> users3 = userDao18.getAll();
        checkSameUser(users3.get(0), user1);
        checkSameUser(users3.get(1), user2);
        checkSameUser(users3.get(2), user3);
        assertThat(users3.size(), is(3));
    }

    public void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
    }
}
