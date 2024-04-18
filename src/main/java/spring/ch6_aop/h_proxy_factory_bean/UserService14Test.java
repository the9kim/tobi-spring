package spring.ch6_aop.h_proxy_factory_bean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import spring.ch5_service_abstraction.Level;
import spring.ch5_service_abstraction.h_mail_service.User3;
import spring.ch5_service_abstraction.h_mail_service.UserDao3;
import spring.ch6_aop.a_extraction_of_transaction.UserService;
import spring.ch6_aop.a_extraction_of_transaction.UserService9Impl;
import spring.ch6_aop.g_factory_bean.TxProxyFactoryBean;

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
@ContextConfiguration("/UserService14-Test-applicationContext.xml")
public class UserService14Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch6_aop.h_proxy_factory_bean.UserService14Test");
    }

    static class TestUserService9 extends UserService9Impl {
        private String id;

        private TestUserService9(String id) {
            this.id = id;
        }

        @Override
        public void upgradeLevel(User3 user) throws SQLException {
            if (user.getId().equals(this.id)) {
                throw new UserService14Test.TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }

    @Autowired
    ApplicationContext context;

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

        userService9Impl.add(nonLeveled);
        userService9Impl.add(leveled);

        User3 savedNonLeveled = userDaoJdbc3.get(nonLeveled.getId());
        User3 savedLeveled = userDaoJdbc3.get(leveled.getId());

        assertThat(savedNonLeveled.getLevel(), is(Level.BASIC));
        assertThat(savedLeveled.getLevel(), is(leveled.getLevel()));
    }


    @Test
    @DirtiesContext
    public void upgradeLevels() throws SQLException {
        MailSender mockMailSender = mock(MailSender.class);
        userService9Impl.setMailSender(mockMailSender);

        UserDao3 mockUserDao = mock(UserDao3.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userService9Impl.setUserDao3(mockUserDao);

        userService9Impl.upgradeLevels();

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
        User3 savedUser = userDaoJdbc3.get(user.getId());
        if (upgraded) {
            assertThat(savedUser.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(savedUser.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws SQLException {

        UserService9Impl target = new UserService14Test.TestUserService9(users.get(3).getId());
        target.setUserDao3(this.userDaoJdbc3);
        target.setMailSender(this.mailSender);

        ProxyFactoryBean pfBean = context.getBean("&userService", ProxyFactoryBean.class);
        pfBean.setTarget(target);
        UserService proxy = (UserService) pfBean.getObject();

        userDaoJdbc3.deleteAll();

        for (User3 user : users) {
            userDaoJdbc3.add(user);
        }

        try {
            proxy.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (UserService14Test.TestUserServiceException e) {
        }
        checkLevel(users.get(1), false);
    }

}
