package spring.ch7_sql_separation.m_annotation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.h_mail_service.User3;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;
import spring.ch6_aop.a_extraction_of_transaction.UserService9Impl;
import spring.ch6_aop.l_transaction_attribute.UserService2;
import spring.ch6_aop.m_annotation_transaction.UserService11Impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static spring.ch5_service_abstraction.c_refactoring_oop.UserService3.MIN_LOGCOUNT_FOR_SILVER;
import static spring.ch5_service_abstraction.c_refactoring_oop.UserService3.MIN_RECOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationContext.class)
public class UserService32Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch7_sql_separation.m_annotation.UserService32Test");
    }

    public static class TestUserService11Impl extends UserService11Impl {
        private String id = "4";

        @Override
        public void upgradeLevel(User3 user) throws SQLException {
            if (user.getId().equals(this.id)) {
                throw new UserService32Test.TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }

    @Autowired
    UserService2 userService;

    // Since a proxy object will be inserted in this variable, the type should be an interface the proxy implement
    @Autowired
    UserService2 testUserService11Impl;

    @Autowired
    UserDao3 userDaoJdbc4;

    @Autowired
    PlatformTransactionManager transactionManager;

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
    @Transactional
    public void add() {
        userDaoJdbc4.deleteAll();
        User3 nonLeveled = users.get(0);
        nonLeveled.setLevel(null);

        User3 leveled = users.get(4);

        userService.add(nonLeveled);
        userService.add(leveled);

        User3 savedNonLeveled = userDaoJdbc4.get(nonLeveled.getId());
        User3 savedLeveled = userDaoJdbc4.get(leveled.getId());

        assertThat(savedNonLeveled.getLevel(), is(Level.BASIC));
        assertThat(savedLeveled.getLevel(), is(leveled.getLevel()));
    }

    @Test
    @Transactional
    public void upgradeLevels() throws SQLException {
        UserService9Impl userService = new UserService9Impl();
        MailSender mockMailSender = mock(MailSender.class);
        userService.setMailSender(mockMailSender);

        UserDao3 mockUserDao = mock(UserDao3.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userService.setUserDao3(mockUserDao);

        userService.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User3.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel(), is(Level.SILVER));
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel(), is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
    }

    public void checkLevel(User3 user, boolean upgraded) {
        User3 savedUser = userDaoJdbc4.get(user.getId());
        if (upgraded) {
            assertThat(savedUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(savedUser.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void upgradeAllOrNothing() throws SQLException {
        userDaoJdbc4.deleteAll();

        for (User3 user : users) {
            userDaoJdbc4.add(user);
        }

        try {
            this.testUserService11Impl.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (UserService32Test.TestUserServiceException e) {
        }
        checkLevel(users.get(1), false);
        userDaoJdbc4.deleteAll();
    }

    @Test(expected = TransientDataAccessResourceException.class)
    public void transactionSyncReadOnly() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setReadOnly(true);
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);

        userService.deleteAll();
    }

    @Test
    public void transactionSyncRollBack() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);

        try {
            userService.deleteAll();
            userService.add(users.get(0));
            userService.add(users.get(1));
        } finally {
            transactionManager.rollback(status);
        }
    }

}
