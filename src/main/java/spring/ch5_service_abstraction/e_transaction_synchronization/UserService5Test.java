package spring.ch5_service_abstraction.e_transaction_synchronization;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;
import spring.ch5_service_abstraction.a_level_management_function.UserDaoJdbc2;
import spring.ch5_service_abstraction.d_transaction_demarcation.UserService4;
import spring.ch5_service_abstraction.d_transaction_demarcation.UserService4Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static spring.ch5_service_abstraction.c_refactoring_oop.UserService3.MIN_LOGCOUNT_FOR_SILVER;
import static spring.ch5_service_abstraction.c_refactoring_oop.UserService3.MIN_RECOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/UserService5-applicationContext.xml")
public class UserService5Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch5_service_abstraction.e_transaction_synchronization.UserService5Test");
    }

    static class TestUserService5 extends UserService5 {
        private String id;

        private TestUserService5(String id) {
            this.id = id;
        }

        @Override
        public void upgradeLevel(User2 user) throws SQLException {
            if (user.getId().equals(this.id)) {
                throw new UserService5Test.TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends SQLException {

    }

    @Autowired
    UserService5 userService5;

    @Autowired
    UserDaoJdbc2 userDaoJdbc2;

    @Autowired
    DataSource dataSource;

    List<User2> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User2("1", "roy", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User2("2", "hoy", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER + 1, 0),
                new User2("3", "doy", "1234", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User2("4", "koy", "1234", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User2("5", "joy", "1234", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void add() {
        userDaoJdbc2.deleteAll();

        User2 nonLeveled = users.get(0);
        nonLeveled.setLevel(null);

        User2 leveled = users.get(4);

        userService5.add(nonLeveled);
        userService5.add(leveled);

        User2 savedNonLeveled = userDaoJdbc2.get(nonLeveled.getId());
        User2 savedLeveled = userDaoJdbc2.get(leveled.getId());

        assertThat(savedNonLeveled.getLevel(), is(Level.BASIC));
        assertThat(savedLeveled.getLevel(), is(leveled.getLevel()));
    }

    @Test
    public void upgradeLevels() throws SQLException {
        userDaoJdbc2.deleteAll();

        for (User2 user : users) {
            userDaoJdbc2.add(user);
        }

        userService5.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);
    }

    public void checkLevel(User2 user, boolean upgraded) {
        User2 savedUser = userDaoJdbc2.get(user.getId());
        if (upgraded) {
            assertThat(savedUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(savedUser.getLevel(), is(user.getLevel()));
        }
    }


    @Test
    public void upgradeAllOrNothing() throws SQLException {
        UserService5 testUserService = new UserService5Test.TestUserService5(users.get(3).getId());
        testUserService.setUserDao2(this.userDaoJdbc2);
        testUserService.setDataSource(this.dataSource);


        userDaoJdbc2.deleteAll();

        for (User2 user : users) {
            userDaoJdbc2.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (UserService5Test.TestUserServiceException e) {
        }
        checkLevel(users.get(1), false);
    }

}
