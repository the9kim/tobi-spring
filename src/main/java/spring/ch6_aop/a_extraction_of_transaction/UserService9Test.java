package spring.ch6_aop.a_extraction_of_transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.h_mail_service.MockMailSender;
import spring.ch5_service_abstraction.h_mail_service.User3;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;
import spring.ch5_service_abstraction.h_mail_service.UserDaoJdbc3;
import spring.ch5_service_abstraction.h_mail_service.UserService8;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static spring.ch5_service_abstraction.c_refactoring_oop.UserService3.MIN_LOGCOUNT_FOR_SILVER;
import static spring.ch5_service_abstraction.c_refactoring_oop.UserService3.MIN_RECOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/UserService9-Test-applicationContext.xml")
public class UserService9Test {

    public static void main(String[] args) {
        JUnitCore.main( "spring.ch6_aop.a_extraction_of_transaction.UserService9Test");
    }

    static class TestUserService9 extends UserService9Impl {
        private String id;

        private TestUserService9(String id) {
            this.id = id;
        }

        @Override
        public void upgradeLevel(User3 user) throws SQLException {
            if (user.getId().equals(this.id)) {
                throw new UserService9Test.TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends SQLException {

    }

    @Autowired
    UserService userService;

    @Autowired
    UserService9Impl userService9Impl;

    @Autowired
    UserDao3 userDaoJdbc3;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    List<User3> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User3("1", "roy", "1234", "a@email.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User3("2", "hoy", "1234", "b@email.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER + 1, 0),
                new User3("3", "doy", "1234", "c@email.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User3("4", "koy", "1234", "d@email.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User3("5", "joy", "1234", "e@email.com", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void add() {
        userDaoJdbc3.deleteAll();

        User3 nonLeveled = users.get(0);
        nonLeveled.setLevel(null);

        User3 leveled = users.get(4);

        userService.add(nonLeveled);
        userService.add(leveled);

        User3 savedNonLeveled = userDaoJdbc3.get(nonLeveled.getId());
        User3 savedLeveled = userDaoJdbc3.get(leveled.getId());

        assertThat(savedNonLeveled.getLevel(), is(Level.BASIC));
        assertThat(savedLeveled.getLevel(), is(leveled.getLevel()));
    }

    @Test
    @DirtiesContext
    public void upgradeLevels() throws SQLException {
        userDaoJdbc3.deleteAll();

        for (User3 user : users) {
            userDaoJdbc3.add(user);
        }

        MockMailSender mockMailSender = new MockMailSender();
        userService9Impl.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);

        List<String> requests = mockMailSender.getRequests();

        assertThat(requests.size(), is(2));
        assertThat(requests.get(0), is(users.get(1).getEmail()));
        assertThat(requests.get(1), is(users.get(3).getEmail()));
    }

    public void checkLevel(User3 user, boolean upgraded) {
        User3 savedUser = userDaoJdbc3.get(user.getId());
        if (upgraded) {
            assertThat(savedUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(savedUser.getLevel(), is(user.getLevel()));
        }
    }


    @Test
    public void upgradeAllOrNothing() throws SQLException {
        UserService9Impl testUserService = new UserService9Test.TestUserService9(users.get(3).getId());
        testUserService.setUserDao3(this.userDaoJdbc3);
        testUserService.setMailSender(this.mailSender);

        UserService9TX userService9TX = new UserService9TX();
        userService9TX.setTransactionManager(this.transactionManager);
        userService9TX.setUserService(testUserService);

        userDaoJdbc3.deleteAll();

        for (User3 user : users) {
            userDaoJdbc3.add(user);
        }

        try {
            userService9TX.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (UserService9Test.TestUserServiceException e) {
        }
        checkLevel(users.get(1), false);
    }

}
