package spring.ch2_test.e_testing_and_DI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch1_object_and_dependency.User;
import spring.ch2_test.c_testing_principles.UserDao9;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/dao9-applicationContext.xml")
@DirtiesContext
public class ManualDI {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch2_test.e_testing_and_DI.ManualDI");
    }

    @Autowired
    private UserDao9 userDao9;
    private User user1;
    private User user2;
    private User user3;


    // Manual Dependency Injection
    @Before
    public void setUp() {
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/spring", "root", "kdw71007050^", true);
        userDao9.setDataSource(dataSource);
        user1 = new User("1", "roy", "1234");
        user2 = new User("2", "hoy", "1234");
        user3 = new User("3", "doy", "1234");

        System.out.println(userDao9.getDataSource());
    }

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
