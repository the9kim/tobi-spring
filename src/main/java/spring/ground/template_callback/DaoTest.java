package spring.ground.template_callback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import spring.ch1_object_and_dependency.User;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DaoTest {

    public static void main(String[] args) {
        JUnitCore.main("spring.ground.template_callback.DaoTest");
    }

    @Autowired
    private Dao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/spring",
                "root",
                "kdw71007050^",
                true
        );
        dao = new Dao();
        dao.setDataSource(dataSource);
        user1 = new User("1", "roy", "1234");
        user2 = new User("2", "hoy", "1234");
        user3 = new User("3", "doy", "1234");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        dao.deleteAll();

        System.out.println("s");

        dao.add(user1);
        dao.add(user2);

        User savedUser1 = dao.get(user1.getId());
        assertThat(savedUser1.getName(), is(user1.getName()));
        assertThat(savedUser1.getPassword(), is(user1.getPassword()));

        User savedUser2 = dao.get(user2.getId());
        assertThat(savedUser2.getName(), is(user2.getName()));
        assertThat(savedUser2.getPassword(), is(user2.getPassword()));
    }
}
