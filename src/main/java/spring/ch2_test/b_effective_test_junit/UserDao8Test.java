package spring.ch2_test.b_effective_test_junit;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import spring.ch1_object_and_dependency.User;
import spring.ch1_object_and_dependency.h_xml.UserDao8;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDao8Test {

    public static void main(String[] args) {
        JUnitCore.main("spring.ch2_test.b_effective_test_junit.UserDao8Test");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new GenericXmlApplicationContext("dao8-applicationContext.xml");
        UserDao8 userDao8 = context.getBean("userDao8", UserDao8.class);

        User user = new User("1", "roy", "1234");
        userDao8.add(user);

        User savedUser = userDao8.get(user.getId());

        assertThat(savedUser.getName(), is(user.getName()));
        assertThat(savedUser.getPassword(), is(user.getPassword()));
    }
}
