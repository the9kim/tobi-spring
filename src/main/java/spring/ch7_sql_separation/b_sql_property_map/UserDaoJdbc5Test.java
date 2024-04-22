package spring.ch7_sql_separation.b_sql_property_map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.h_mail_service.User3;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/UserService22-Test-applicationContext.xml")
public class UserDaoJdbc5Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch7_sql_separation.b_sql_property_map.UserDaoJdbc5Test");
    }

    @Autowired
    private UserDao3 userDao3;

    private User3 user1;
    private User3 user2;
    private User3 user3;

    @Before
    public void setUp() {
        user1 = new User3("1", "roy", "1234", "a@email.com", Level.BASIC, 1, 0);
        user2 = new User3("2", "hoy", "1234", "b@email.com", Level.SILVER, 55, 10);
        user3 = new User3("3", "doy", "1234", "c@email.com", Level.GOLD, 100, 40);
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        userDao3.deleteAll();
        assertThat(userDao3.getCount(), is(0));

        userDao3.add(user1);
        userDao3.add(user2);
        assertThat(userDao3.getCount(), is(2));

        User3 savedUser1 = userDao3.get(user1.getId());
        checkSameUser(user1, savedUser1);

        User3 savedUser2 = userDao3.get(user2.getId());
        checkSameUser(user2, savedUser2);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        userDao3.deleteAll();
        assertThat(userDao3.getCount(), is(0));

        userDao3.get("unknown_id");
    }

    @Test
    public void getCount() throws SQLException {
        userDao3.deleteAll();
        assertThat(userDao3.getCount(), is(0));

        userDao3.add(user1);
        assertThat(userDao3.getCount(), is(1));

        userDao3.add(user2);
        assertThat(userDao3.getCount(), is(2));

        userDao3.add(user3);
        assertThat(userDao3.getCount(), is(3));
    }

    @Test
    public void getAll() throws SQLException {
        userDao3.deleteAll();

        List<User3> users0 = userDao3.getAll();
        assertThat(users0.size(), is(0));

        userDao3.add(user1);
        List<User3> users1 = userDao3.getAll();
        checkSameUser(users1.get(0), user1);
        assertThat(users1.size(), is(1));

        userDao3.add(user2);
        List<User3> users2 = userDao3.getAll();
        checkSameUser(users2.get(0), user1);
        checkSameUser(users2.get(1), user2);
        assertThat(users2.size(), is(2));

        userDao3.add(user3);
        List<User3> users3 = userDao3.getAll();
        checkSameUser(users3.get(0), user1);
        checkSameUser(users3.get(1), user2);
        checkSameUser(users3.get(2), user3);
        assertThat(users3.size(), is(3));
    }

    @Test
    public void update() {
        userDao3.deleteAll();

        userDao3.add(user1);
        userDao3.add(user2);

        user1.setName("koy");
        user1.setPassword("5678");
        user1.setEmail("new@email.com");
        user1.setLevel(Level.SILVER);
        user1.setLogin(55);
        user1.setRecommend(10);

        userDao3.update(user1);
        User3 savedUser1 = userDao3.get(user1.getId());
        User3 savedUser2 = userDao3.get(user2.getId());

        checkSameUser(user1, savedUser1);
        checkSameUser(user2, savedUser2);
    }

    public void checkSameUser(User3 user1, User3 user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }
}
