package spring.ch4_exception.b_spring_exception_learning_test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch1_object_and_dependency.User;
import spring.ch4_exception.a_dao_interface.UserDao;

import javax.sql.DataSource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/daoJdbc-applicationContext.xml")
public class SpringExceptionLearningTest {

    @Autowired
    UserDao userDao;

    @Autowired
    DataSource dataSource;

    private User user1;

    @Before
    public void setUp() {
        user1 = new User("1", "roy", "1234");
    }

    @Test(expected = DuplicateKeyException.class)
    public void duplicateKeyException() {
        userDao.add(user1);
        userDao.add(user1);
    }
}
