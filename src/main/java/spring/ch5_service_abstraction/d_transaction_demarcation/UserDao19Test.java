package spring.ch5_service_abstraction.d_transaction_demarcation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch1_object_and_dependency.User;
import spring.ch4_exception.a_dao_interface.UserDao;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/UserService4-applicationContext.xml")
public class UserDao19Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch5_service_abstraction.d_transaction_demarcation.UserDao19Test");
    }

    @Autowired
    private UserDao19 userDao;

    @Autowired
    private DataSource dataSource;

    private Connection conn;

    private User2 user1;
    private User2 user2;
    private User2 user3;

    @Before
    public void setUp() throws SQLException {
        user1 = new User2("1", "roy", "1234", Level.BASIC, 1, 0);
        user2 = new User2("2", "hoy", "1234", Level.SILVER, 55, 10);
        user3 = new User2("3", "doy", "1234", Level.GOLD, 100, 40);

        conn = dataSource.getConnection();
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        userDao.deleteAll(conn);
        assertThat(userDao.getCount(conn), is(0));

        userDao.add(user1, conn);
        userDao.add(user2, conn);
        assertThat(userDao.getCount(conn), is(2));

        User2 savedUser1 = userDao.get(user1.getId(), conn);
        assertThat(savedUser1.getName(), is(user1.getName()));
        assertThat(savedUser1.getPassword(), is(user1.getPassword()));

        User2 savedUser2 = userDao.get(user2.getId(), conn);
        assertThat(savedUser2.getName(), is(user2.getName()));
        assertThat(savedUser2.getPassword(), is(user2.getPassword()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        userDao.deleteAll(conn);
        assertThat(userDao.getCount(conn), is(0));

        userDao.get("unknown_id", conn);
    }

    @Test
    public void getCount() throws SQLException {
        userDao.deleteAll(conn);
        assertThat(userDao.getCount(conn), is(0));

        userDao.add(user1, conn);
        assertThat(userDao.getCount(conn), is(1));

        userDao.add(user2, conn);
        assertThat(userDao.getCount(conn), is(2));

        userDao.add(user3, conn);
        assertThat(userDao.getCount(conn), is(3));
    }

    // Add a Test case for negative test
    @Test
    public void getAll() throws SQLException {
        userDao.deleteAll(conn);

        List<User2> users0 = userDao.getAll(conn);
        assertThat(users0.size(), is(0));

        userDao.add(user1, conn);
        List<User2> users1 = userDao.getAll(conn);
        checkSameUser(users1.get(0), user1);
        assertThat(users1.size(), is(1));

        userDao.add(user2, conn);
        List<User2> users2 = userDao.getAll(conn);
        checkSameUser(users2.get(0), user1);
        checkSameUser(users2.get(1), user2);
        assertThat(users2.size(), is(2));

        userDao.add(user3, conn);
        List<User2> users3 = userDao.getAll(conn);
        checkSameUser(users3.get(0), user1);
        checkSameUser(users3.get(1), user2);
        checkSameUser(users3.get(2), user3);
        assertThat(users3.size(), is(3));
    }

    @Test
    public void update() throws SQLException {
        userDao.deleteAll(conn);

        userDao.add(user1, conn);
        userDao.add(user2, conn);

        user1.setName("koy");
        user1.setPassword("5678");
        user1.setLevel(Level.SILVER);
        user1.setLogin(55);
        user1.setRecommend(10);

        userDao.update(user1, conn);
        User2 savedUser1 = userDao.get(user1.getId(), conn);
        User2 savedUser2 = userDao.get(user2.getId(), conn);

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
