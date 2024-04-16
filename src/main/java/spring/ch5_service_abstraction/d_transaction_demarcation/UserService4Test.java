package spring.ch5_service_abstraction.d_transaction_demarcation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.User2;

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
@ContextConfiguration("/UserService4-applicationContext.xml")
public class UserService4Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch5_service_abstraction.d_transaction_demarcation.UserService4Test");
    }

    static class TestUserService4 extends UserService4 {
        private String id;

        private TestUserService4(String id) {
            this.id = id;
        }

        @Override
        public void upgradeLevel(User2 user, Connection conn) throws SQLException {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user, conn);
        }
    }

    static class TestUserServiceException extends SQLException {

    }

    @Autowired
    UserService4 userService4;

    @Autowired
    UserDao19 userDao19;

    @Autowired
    DataSource dataSource;

    Connection conn;

    List<User2> users;

    @Before
    public void setUp() throws SQLException {
        users = Arrays.asList(
                new User2("1", "roy", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User2("2", "hoy", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER + 1, 0),
                new User2("3", "doy", "1234", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User2("4", "koy", "1234", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User2("5", "joy", "1234", Level.GOLD, 100, Integer.MAX_VALUE)
        );
        conn = dataSource.getConnection();
    }

    @Test
    public void add() throws SQLException {
        userDao19.deleteAll(conn);

        User2 nonLeveled = users.get(0);
        nonLeveled.setLevel(null);

        User2 leveled = users.get(4);

        userService4.add(nonLeveled);
        userService4.add(leveled);

        User2 savedNonLeveled = userDao19.get(nonLeveled.getId(), conn);
        User2 savedLeveled = userDao19.get(leveled.getId(), conn);

        assertThat(savedNonLeveled.getLevel(), is(Level.BASIC));
        assertThat(savedLeveled.getLevel(), is(leveled.getLevel()));
    }

    @Test
    public void upgradeLevels() throws SQLException {
        userDao19.deleteAll(conn);

        for (User2 user : users) {
            userDao19.add(user, conn);
        }

        userService4.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    public void checkLevelUpgraded(User2 user, boolean upgraded) throws SQLException {
        User2 savedUser = userDao19.get(user.getId(), conn);
        if (upgraded) {
            assertThat(savedUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(savedUser.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void upgradeAllOrNothing() throws SQLException {
        UserService4 testUserService = new TestUserService4(users.get(3).getId());
        testUserService.setUserDao19(this.userDao19);
        testUserService.setDataSource(this.dataSource);


        userDao19.deleteAll(conn);

        for (User2 user : users) {
            userDao19.add(user, conn);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }
        checkLevelUpgraded(users.get(1), false);
    }
}
