package spring.ch5_service_abstraction.a_level_management_function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch1_object_and_dependency.User;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/daoJdbc2-applicationContext.xml")
public class UserDaoJdbc2Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch5_service_abstraction.a_level_management_function.UserDaoJdbc2Test");
    }

    @Autowired
    private UserDao2 userDao2;

    private User2 user1;
    private User2 user2;
    private User2 user3;

    @Before
    public void setUp() {
        user1 = new User2("1", "roy", "1234", Level.BASIC, 1, 0);
        user2 = new User2("2", "hoy", "1234", Level.SILVER, 55, 10);
        user3 = new User2("3", "doy", "1234", Level.GOLD, 100, 40);
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        userDao2.deleteAll();
        assertThat(userDao2.getCount(), is(0));

        userDao2.add(user1);
        userDao2.add(user2);
        assertThat(userDao2.getCount(), is(2));

        User2 savedUser1 = userDao2.get(user1.getId());
        checkSameUser(user1, savedUser1);

        User2 savedUser2 = userDao2.get(user2.getId());
        checkSameUser(user2, savedUser2);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        userDao2.deleteAll();
        assertThat(userDao2.getCount(), is(0));

        userDao2.get("unknown_id");
    }

    @Test
    public void getCount() throws SQLException {
        userDao2.deleteAll();
        assertThat(userDao2.getCount(), is(0));

        userDao2.add(user1);
        assertThat(userDao2.getCount(), is(1));

        userDao2.add(user2);
        assertThat(userDao2.getCount(), is(2));

        userDao2.add(user3);
        assertThat(userDao2.getCount(), is(3));
    }

    @Test
    public void getAll() throws SQLException {
        userDao2.deleteAll();

        List<User2> users0 = userDao2.getAll();
        assertThat(users0.size(), is(0));

        userDao2.add(user1);
        List<User2> users1 = userDao2.getAll();
        checkSameUser(users1.get(0), user1);
        assertThat(users1.size(), is(1));

        userDao2.add(user2);
        List<User2> users2 = userDao2.getAll();
        checkSameUser(users2.get(0), user1);
        checkSameUser(users2.get(1), user2);
        assertThat(users2.size(), is(2));

        userDao2.add(user3);
        List<User2> users3 = userDao2.getAll();
        checkSameUser(users3.get(0), user1);
        checkSameUser(users3.get(1), user2);
        checkSameUser(users3.get(2), user3);
        assertThat(users3.size(), is(3));
    }

    @Test
    public void update() {
        userDao2.deleteAll();

        userDao2.add(user1);
        userDao2.add(user2);

        user1.setName("koy");
        user1.setPassword("5678");
        user1.setLevel(Level.SILVER);
        user1.setLogin(55);
        user1.setRecommend(10);

        userDao2.update(user1);
        User2 savedUser1 = userDao2.get(user1.getId());
        User2 savedUser2 = userDao2.get(user2.getId());

        checkSameUser(user1, savedUser1);
        checkSameUser(user2, savedUser2);
    }

    public void checkSameUser(User2 user1, User2 user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }
}
