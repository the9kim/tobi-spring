package spring.ch1_object_and_dependency.h_xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import spring.ch1_object_and_dependency.User;

import java.sql.SQLException;

public class UserDao8Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ApplicationContext context = new GenericXmlApplicationContext("dao8-applicationContext.xml");

        UserDao8 userDao8 = context.getBean("userDao8", UserDao8.class);

        User user = new User("1", "roy", "1234");
        userDao8.add(user);
        System.out.println("user 등록 성공");

        User savedUser = userDao8.get("1");
        System.out.println("User Name: " + savedUser.getName());
        System.out.println("User Password: " + savedUser.getName());
    }
}
